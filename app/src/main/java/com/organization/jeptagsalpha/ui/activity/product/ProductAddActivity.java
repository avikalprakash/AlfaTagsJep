package com.organization.jeptagsalpha.ui.activity.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.organization.jeptagsalpha.MyConstants;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.base.NfcActivity;
import com.organization.jeptagsalpha.ui.adapter.ProductAddAdapter;
import com.organization.jeptagsalpha.ui.controls.MyTextView;
import com.organization.jeptagsalpha.ui.model.NfcModel;
import com.organization.jeptagsalpha.ui.model.Product;
import com.organization.jeptagsalpha.ui.utils.ProductAddAdpterNotif;
import com.organization.jeptagsalpha.utils.NfcConvertor;
import com.organization.jeptagsalpha.wordpress.model.wc.BatchProduct;
import com.organization.jeptagsalpha.wordpress.rest.HttpServerErrorResponse;
import com.organization.jeptagsalpha.wordpress.rest.WordPressRestResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductAddActivity extends NfcActivity implements ProductAddAdpterNotif {
    @Bind(R.id.productaddlist)
    RecyclerView recyclerView;
    @Bind(R.id.lblQty)
    MyTextView lblQty;
    private List<Product> productList = new ArrayList<>();
    private com.organization.jeptagsalpha.wordpress.model.wc.Product currentProduct;
    ProductAddAdapter productAddAdapter;
    public static void start(Context context, com.organization.jeptagsalpha.wordpress.model.wc.Product product) {
        Intent intent = new Intent(context, ProductAddActivity.class);
        intent.putExtra("product",product);
        ((Activity)context).startActivityForResult(intent,1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);
        setActionBar(getTitle().toString());
        ButterKnife.bind(this);
        currentProduct = (com.organization.jeptagsalpha.wordpress.model.wc.Product) getIntent().getSerializableExtra("product");
        lblQty.setText(getString(R.string.lbl_qty_status,currentProduct.getQuantity(),0));
        productAddAdapter = new ProductAddAdapter(productList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productAddAdapter);


    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            for (String message : getNfcMessages()) {
                NfcModel nfcModel = NfcConvertor.decode(message);
                if (nfcModel == null) {
                    showToast(getString(R.string.tag_not_recogniz_error));
                    return;
                }
                if (nfcModel.getProductId().isEmpty()) {
                    showToast(getString(R.string.productid_empty_error));
                    return;
                }
                addProductToList(nfcModel);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }

    private void addProductToList(NfcModel nfcModel) {
        if(productList.size() < currentProduct.getQuantity()) {
            String id = String.valueOf(new Random().nextInt());
            productList.add(new Product(String.valueOf(nfcModel.getProductId())));
            productAddAdapter.notifyDataSetChanged();
            lblQty.setText(getString(R.string.lbl_qty_status,currentProduct.getQuantity(), (currentProduct.getQuantity() - productList.size() )));
        }
        else
        {
            showToast(getString(R.string.validate_qty_with_total));
        }
    }
    @OnClick(R.id.BtnSaveProduct)
    public void onClickSaveProduct(View view)
    {
        int totalqty = currentProduct.getQuantity();
        if(productList.size() < totalqty)
        {
            showToast(getString(R.string.poduct_nfc_count_error));
            return;
        }
        if(totalqty == productList.size())
        {
            saveBatchProduct(productList);
        }
        else
        {
            showToast(getString(R.string.something_wrong));
            return;
        }
    }

    private void saveBatchProduct(List<Product> productList) {
        ArrayList<com.organization.jeptagsalpha.wordpress.model.wc.Product> arrayProduct = new ArrayList<com.organization.jeptagsalpha.wordpress.model.wc.Product>();
        arrayProduct.add(currentProduct);
        for (Product productid:productList) {
            try {
                com.organization.jeptagsalpha.wordpress.model.wc.Product product = new com.organization.jeptagsalpha.wordpress.model.wc.Product();
                product = (com.organization.jeptagsalpha.wordpress.model.wc.Product)currentProduct.clone();
                product.setId(Integer.parseInt(productid.getId()));
                arrayProduct.add(product);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

        }
        BatchProduct batchProduct = new BatchProduct();
        batchProduct.setUpdate(arrayProduct);
        showProgress(getString(R.string.wait_batch_product_ipdating));
        MyConstants.WCService.updateBatchProduct(batchProduct, new WordPressRestResponse<BatchProduct>() {
            @Override
            public void onSuccess(BatchProduct result) {
                hideProgress();
                setResult(2);
                finish();
            }

            @Override
            public void onFailure(HttpServerErrorResponse errorResponse) {
                hideProgress();
                showHttpError(errorResponse);
            }
        });
    }

    @Override
    public void onProductRemoved(int position) {
        lblQty.setText(getString(R.string.lbl_qty_status,currentProduct.getQuantity(), (currentProduct.getQuantity() - productList.size() )));
    }
}
