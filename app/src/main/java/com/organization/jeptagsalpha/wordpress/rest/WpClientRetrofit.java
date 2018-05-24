package com.organization.jeptagsalpha.wordpress.rest;


import android.content.Context;

import com.organization.jeptagsalpha.MyConstants;
import com.organization.jeptagsalpha.wordpress.WordPressRestInterface;
import com.organization.jeptagsalpha.wordpress.model.Media;
import com.organization.jeptagsalpha.wordpress.model.Meta;
import com.organization.jeptagsalpha.wordpress.model.Post;
import com.organization.jeptagsalpha.wordpress.model.Taxonomy;
import com.organization.jeptagsalpha.wordpress.model.Token;
import com.organization.jeptagsalpha.wordpress.model.User;
import com.organization.jeptagsalpha.wordpress.model.dto.PostCount;
import com.organization.jeptagsalpha.wordpress.model.wc.BatchProduct;
import com.organization.jeptagsalpha.wordpress.model.wc.Category;
import com.organization.jeptagsalpha.wordpress.model.wc.Product;
import com.organization.jeptagsalpha.wordpress.rest.interceptor.OkHttpBasicAuthInterceptor;
import com.organization.jeptagsalpha.wordpress.rest.interceptor.OkHttpDebugInterceptor;
import com.organization.jeptagsalpha.wordpress.util.ContentUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Jan-Louis Crafford
 *         Created on 2016/01/12.
 */
public class WpClientRetrofit {

    private WordPressRestInterface mRestInterface;
    private Context context;

    /*public WpClientRetrofit(String baseUrl,boolean debugEnabled) {
        this(baseUrl, , "","",false, debugEnabled);
    }*/

    public WpClientRetrofit(Context context, String baseUrl, final String username, final String password, boolean debugEnabled) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        this.context = context;
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);

        // add the Basic Auth header
        builder.addInterceptor(new OkHttpBasicAuthInterceptor(username, password));

        if (debugEnabled) {
            builder.addInterceptor(new OkHttpDebugInterceptor(false,false,false));
        }

        // setup retrofit with custom OkHttp client and Gson parser

        try {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build()).build();
            mRestInterface = retrofit.create(WordPressRestInterface.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        // create instance of REST interface

    }

    private <T> void doRetrofitCall(Call<T> call, final WordPressRestResponse<T> callback) {

        Callback<T> retroCallback = new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(HttpServerErrorResponse.from(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                t.printStackTrace();
                callback.onFailure(HttpServerErrorResponse.from(t));
            }
        };
        call.enqueue(retroCallback);
    }

    // USER
    public void generateToken(String username,String password, WordPressRestResponse<Token> callback) {
        Map<String, Object> data = new HashMap<>() ;
        data.put("username",username);
        data.put("password",password);
        doRetrofitCall(mRestInterface.generateToken(data), callback);
    }
    public void createUser(User user, WordPressRestResponse<User> callback) {
        doRetrofitCall(mRestInterface.createUser(user),callback);
    }

    public void getUser(long userId, WordPressRestResponse<User> callback) {
        doRetrofitCall(mRestInterface.getUser(userId),callback);
    }


    public void getUserFromLogin(String login, WordPressRestResponse<User> callback) {
        doRetrofitCall(mRestInterface.getUserFromLogin(login), callback);
    }

    public void getUserFromEmail(String email, WordPressRestResponse<User> callback) {
        doRetrofitCall(mRestInterface.getUserFromEmail(email), callback);
    }

    public void getUserMe(WordPressRestResponse<User> callback) {
        doRetrofitCall(mRestInterface.getUserMe(), callback);
    }

    public void updateUser(User user, WordPressRestResponse<User> callback) {
        doRetrofitCall(mRestInterface.updateUser(Long.parseLong(user.getId()),user), callback);
    }

    // POSTS

    public void createPost(Post post, WordPressRestResponse<Post> callback) {
        // 201 CREATED on success
        doRetrofitCall(mRestInterface.createPost(Post.mapFromFields(post)), callback);
    }

    public Call<Post> createPost(Post post) {
        return mRestInterface.createPost(Post.mapFromFields(post));
    }

    public void getPost(long postId, WordPressRestResponse<Post> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("context", "edit");
        doRetrofitCall(mRestInterface.getPost(postId, map), callback);
    }

    public Call<Post> getPost(long postId) {
        Map<String, String> map = new HashMap<>();
        map.put("context", "edit");
        return mRestInterface.getPost(postId, map);
    }

    public void getPostForEdit(long postId, WordPressRestResponse<Post> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("context", "edit");
        doRetrofitCall(mRestInterface.getPost(postId, map), callback);
    }

    public void getPosts(WordPressRestResponse<List<Post>> callback) {
        doRetrofitCall(mRestInterface.getPosts(), callback);
    }

    public Call<List<Post>> getPosts() {
        return mRestInterface.getPosts();
    }

    public Call<List<Post>> getPostsForPage(int startPage) {
        Map<String, String> map = new HashMap<>();
        map.put("page", startPage + "");
        map.put("context", "edit");
        return mRestInterface.getPosts(map);
    }

    public Call<List<Post>> getPostsAfterDate(String date) {
        Map<String, String> map = new HashMap<>();
        map.put("after", date);
        map.put("context", "edit");
        return mRestInterface.getPosts(map);
    }

    public Call<List<Post>> getPostsForAuthor(long authorId, String status) {
        return mRestInterface.getPostsForAuthor(authorId, status, "edit");
    }

    public void getPostsForAuthor(long authorId, String status, WordPressRestResponse<List<Post>> callback) {
        doRetrofitCall(getPostsForAuthor(authorId, status), callback);
    }

    public void getPostsForTag(String tag, WordPressRestResponse<List<Post>> callback) {
        doRetrofitCall(mRestInterface.getPostsForTags(tag), callback);
    }

    public void updatePost(Post post, WordPressRestResponse<Post> callback) {
        // 200 on success
        doRetrofitCall(mRestInterface.updatePost(post.getId(), Post.mapFromFields(post)), callback);
    }

    public Call<Post> updatePost(Post post) {
        return mRestInterface.updatePost(post.getId(), Post.mapFromFields(post));
    }

    public void deletePost(long postId, boolean force, WordPressRestResponse<Post> callback) {
        // 200 on success
        // 410 GONE on failure
        doRetrofitCall(mRestInterface.deletePost(postId, force, "edit"), callback);
    }

    public Call<Post> deletePost(long postId, boolean force) {
        return mRestInterface.deletePost(postId, force, "edit");
    }

    /* MEDIA */

    public void createMedia(Media media, File file, WordPressRestResponse<Media> callback) {
        Map<String, RequestBody> map = ContentUtil.makeMediaItemUploadMap(media, file);
        String header = "filename=" + file.getName();

        doRetrofitCall(mRestInterface.createMedia(header, map), callback);
    }

    public Call<Media> createMedia(Media media, File file) {
        Map<String, RequestBody> map = ContentUtil.makeMediaItemUploadMap(media, file);
        String header = "filename=" + file.getName();
        return mRestInterface.createMedia(header, map);
    }

    public void getMedia(WordPressRestResponse<List<Media>> callback) {
        doRetrofitCall(mRestInterface.getMedia(), callback);
    }

    public Call<Media> getMedia(long mediaId) {
        return mRestInterface.getMedia(mediaId);
    }

    public void getMediaForPost(long postId, String mimeType, WordPressRestResponse<List<Media>> callback) {
        doRetrofitCall(mRestInterface.getMediaForPost(postId, mimeType), callback);
    }

    public Call<List<Media>> getMediaForPost(long postId, String mimeType) {
        return mRestInterface.getMediaForPost(postId, mimeType);
    }

    public void updateMedia(Media media, long mediaId, WordPressRestResponse<Media> callback) {
        doRetrofitCall(mRestInterface.updateMedia(mediaId, Media.mapFromFields(media)), callback);
    }

    public Call<Media> updateMedia(Media media, long mediaId) {
        return mRestInterface.updateMedia(mediaId, Media.mapFromFields(media));
    }

    public Call<Media> deleteMedia(long mediaId) {
        return mRestInterface.deleteMedia(mediaId);
    }

    /* TAXONOMIES */

    public void setTagForPost(long postId, long tagId, WordPressRestResponse<Taxonomy> callback) {
        doRetrofitCall(mRestInterface.setPostTag(postId, tagId), callback);
    }

    public void getTagsForPost(long postId, WordPressRestResponse<List<Taxonomy>> callback) {
        doRetrofitCall(mRestInterface.getPostTags(postId), callback);
    }

    public void getTags(WordPressRestResponse<List<Taxonomy>> callback) {
        doRetrofitCall(mRestInterface.getTags(), callback);
    }

    public void getTagsOrderedByCount(WordPressRestResponse<List<Taxonomy>> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("orderby", "count");
        map.put("order", "desc");

        doRetrofitCall(mRestInterface.getTagsOrdered(map), callback);
    }


    public void setCategoryForPost(long postId, long catId, WordPressRestResponse<Taxonomy> callback) {
        doRetrofitCall(mRestInterface.setPostCategory(postId, catId), callback);
    }

    public Call<Taxonomy> setCategoryForPost(long postId, long catId) {
        return mRestInterface.setPostCategory(postId, catId);
    }

    public void getCategoriesForPost(long postId, WordPressRestResponse<List<Taxonomy>> callback) {
        doRetrofitCall(mRestInterface.getPostCategories(postId), callback);
    }

    public void getCategories(WordPressRestResponse<List<Taxonomy>> callback) {
        doRetrofitCall(mRestInterface.getCategories(), callback);
    }

    public Call<List<Taxonomy>> getCategories() {
        return mRestInterface.getCategories();
    }

    public void getCategoriesForParent(long parentId, WordPressRestResponse<List<Taxonomy>> callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("parent", parentId);

        doRetrofitCall(mRestInterface.getCategories(map), callback);
    }

    public Call<List<Taxonomy>> getCategoriesForParent(long parentId) {
        Map<String, Object> map = new HashMap<>();
        map.put("parent", parentId);
        return mRestInterface.getCategories(map);
    }

    /* META */

    public void createPostMeta(long postId, Meta meta, WordPressRestResponse<Meta> callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", meta.getKey());
        map.put("value", meta.getValue());

        doRetrofitCall(mRestInterface.createPostMeta(postId, map), callback);
    }

    public Call<Meta> createPostMeta(long postId, Meta meta) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", meta.getKey());
        map.put("value", meta.getValue());

        return mRestInterface.createPostMeta(postId, map);
    }

    public Call<Meta> updatePostMeta(long postId, Meta meta) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", meta.getKey());
        map.put("value", meta.getValue());

        return mRestInterface.updatePostMeta(postId, meta.getId(), map);
    }

    public Call<List<Meta>> getPostMetas(long postId) {
        return mRestInterface.getPostMeta(postId);
    }

    public Call<Meta> deletePostMeta(long postId, long metaId) {
        return mRestInterface.deletePostMeta(postId, metaId);
    }

    /* OTHER */

    public void getPostCounts(WordPressRestResponse<PostCount> callback) {
        doRetrofitCall(mRestInterface.getPostCounts(), callback);
    }

    public void createProduct(Product product, WordPressRestResponse<Product> callback)
    {
        doRetrofitCall(mRestInterface.createProduct(product),callback);
    }
    public void updateProduct(Product product, WordPressRestResponse<Product> callback)
    {
        doRetrofitCall(mRestInterface.updateProduct(product.getId(), product),callback);
    }
    public void getProduct(String productId, WordPressRestResponse<Product> callback)
    {
        int prodid = Integer.parseInt(productId);
        doRetrofitCall(mRestInterface.getProduct(prodid),callback);
    }
    public void getProductListByUser(String userid,WordPressRestResponse<List<Product>> callback)
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("attribute", "pa_"+MyConstants.ATTRIBUTE_CUST_ID_NAME);
        params.put("attribute_term_name", userid);
        doRetrofitCall(mRestInterface.getProductListByUser(params),callback);
    }
    public void updateBatchProduct(BatchProduct batchProduct,WordPressRestResponse<BatchProduct> callback)
    {
        doRetrofitCall(mRestInterface.batchProductUpdate(batchProduct),callback);
    }
    public Call<PostCount> getPostCounts() {
        return mRestInterface.getPostCounts();
    }
    public void getProductCategories(WordPressRestResponse<List<Category>> categoryWordPressRestResponse)
    {
        doRetrofitCall(mRestInterface.getProductCategories(),categoryWordPressRestResponse);
    }
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
