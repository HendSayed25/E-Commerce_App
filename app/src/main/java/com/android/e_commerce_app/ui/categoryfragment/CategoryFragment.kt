package com.android.e_commerce_app.ui.categoryfragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.e_commerce_app.R
import com.android.e_commerce_app.base.BaseFragment
import com.android.e_commerce_app.database.MyDataBase
import com.android.e_commerce_app.databinding.FragmentCategoryBinding
import com.android.e_commerce_app.ui.CategoryClickListener
import com.android.e_commerce_app.ui.ClickListener
import com.android.e_commerce_app.ui.api.ProductsItem
import com.android.e_commerce_app.ui.home_fragment.HomeAdapter
import com.android.e_commerce_app.ui.productdetailsfragment.ProductDetailsFragment

class CategoryFragment :BaseFragment<CategoryViewModel,FragmentCategoryBinding>() {

//    companion object {
//        fun newInstance() = CategoryFragment()
//    }

    var categoryAdapter=CategoryAdapter(null)
    var homeAdapter= HomeAdapter(null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databinding.civm=viewModel

        observation()

        viewModel.get_Category()

        databinding.categoryTypeRecycler.adapter=categoryAdapter

        click()



    }

    override fun get_layout(): Int {
        return R.layout.fragment_category
    }

    override fun get_viewModel(): CategoryViewModel {
        return ViewModelProvider(this).get(CategoryViewModel::class.java)
    }

    fun click(){
        categoryAdapter.categoryListener=object : CategoryClickListener {

            override fun categoryClick(position: Int, category_name: String?) {

                viewModel.get_product_by_category(category_name!!)
                databinding.categoryProductrecyclerView.adapter=homeAdapter


            }

        }
        homeAdapter.product_Clicked=object : ClickListener {
            lateinit var Product: ProductsItem


            override fun add_FavClick(position: Int, item: ProductsItem?, flag:Int) {


                if(flag%2==0) {
                    Product = ProductsItem(true,item?.addNumber,item?.addToCart,item?.thumbnail,item?.rating,item?.description,
                        item?.title,item?.price,item?.id!!,item.stock,0)
                }
                else{
                    Product = ProductsItem(false,item?.addNumber,item?.addToCart,item?.thumbnail,item?.rating,item?.description,
                        item?.title,item?.price,item?.id!!,item.stock,0)

                }

                MyDataBase.getDataBase().productDao().insertProductsToDataBase(Product)

                // val fav_products= MyDataBase.getDataBase().productDao().getFavProduct(true)


                //Log.e("Favvvvvvvvvvvvvvvv","correctttttt${fav_products}")



            }

            override fun add_Item(item: ProductsItem?, add: Int?) {

                Product = ProductsItem(item?.favOrNot,add,item?.addToCart,item?.thumbnail,item?.rating,item?.description,
                    item?.title,item?.price,item?.id!!,item.stock,0)


                MyDataBase.getDataBase().productDao().insertProductsToDataBase(Product)

                // val get_product= MyDataBase.getDataBase().productDao().getProduct(item.id)

                //Log.e("addItemmmmmmmmmmmmmm","correctttttt${get_product}")

            }

            override fun add_Cart(item: ProductsItem?) {

                Product = ProductsItem(item?.favOrNot,item?.addNumber,true,item?.thumbnail,item?.rating,item?.description,
                    item?.title,item?.price,item?.id!!,item.stock,0)


                MyDataBase.getDataBase().productDao().insertProductsToDataBase(Product)

                // val get_product= MyDataBase.getDataBase().productDao().getProduct(item.id)

                //Log.e("addCarttttttttt","correctttttt${get_product}")

            }

            override fun add_minesButton(item: ProductsItem?, add:Int?) {
                TODO("Not yet implemented")
            }

            override fun addHolder(position: Int, item: ProductsItem?) {

                //to transfer details of products

                val bundle=Bundle().apply {
                    putSerializable("product_object",item)
                }

                val secondFragment = ProductDetailsFragment().apply {
                    arguments = bundle
                }
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container_frame, secondFragment) // استبدال fragment_container بالـ ID الصحيح لحاوية الـ Fragment
                    .addToBackStack(null) // إضافة المعاملة إلى الـ BackStack حتى يمكن للمستخدم العودة للـ Fragment الأول
                    .commit()
            }

        }
    }


    fun observation(){

        viewModel.product_by_category.observe(viewLifecycleOwner, Observer {
            homeAdapter.notify(it)
        })

        viewModel.categorys.observe(viewLifecycleOwner, Observer {category->


            categoryAdapter.notify(category)

        })

        viewModel.progress.observe(viewLifecycleOwner, Observer {

            databinding.categoryProgressbar.isVisible=it
        })
    }


}