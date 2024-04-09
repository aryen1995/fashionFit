package com.example.fashionFit.presentation.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProviders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.fashionFit.R
import com.example.fashionFit.data.local.room.CartViewModel
import com.example.fashionFit.data.local.room.ProductEntity
import com.example.fashionFit.presentation.activity.PaymentMethodActivity
import com.example.fashionFit.presentation.activity.SettingsActivity
import com.example.fashionFit.presentation.adapter.CartAdapter
import com.example.fashionFit.presentation.adapter.CartItemClickAdapter


class BagFragment : Fragment(), CartItemClickAdapter {

    lateinit var cartRecView:RecyclerView
    lateinit var cartAdapter: CartAdapter
    lateinit var animationView: LottieAnimationView
    lateinit var totalPriceBagFrag:TextView
    lateinit var Item: ArrayList<ProductEntity>
     var sum:Int = 0


    private lateinit var cartViewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_bag, container, false)

        cartRecView = view.findViewById(R.id.cartRecView)
        animationView = view.findViewById(R.id.animationViewCartPage)
        totalPriceBagFrag = view.findViewById(R.id.totalPriceBagFrag)
        val checkOut_BagPage= view.findViewById<Button>(R.id.checkOut_BagPage)
        val bottomCartLayout:LinearLayout = view.findViewById(R.id.bottomCartLayout)
        val emptyBagMsgLayout:LinearLayout = view.findViewById(R.id.emptyBagMsgLayout)
        val MybagText:TextView = view.findViewById(R.id.MybagText)
        Item = arrayListOf()


        animationView.playAnimation()
        animationView.loop(true)
        bottomCartLayout.visibility = View.GONE
        MybagText.visibility = View.GONE
        emptyBagMsgLayout.visibility = View.VISIBLE


        cartRecView.layoutManager = LinearLayoutManager(context)
        cartAdapter = CartAdapter(activity as Context, this )
        cartRecView.adapter = cartAdapter


        cartViewModel = ViewModelProviders.of(this).get(CartViewModel::class.java)

        cartViewModel.allproducts.observe(viewLifecycleOwner, Observer {List ->
            List?.let {
                cartAdapter.updateList(it)
                Item.clear()
                sum = 0
                Item.addAll(it)
            }

            if (List.size == 0){
                animationView.playAnimation()
                animationView.loop(true)
                bottomCartLayout.visibility = View.GONE
                MybagText.visibility = View.GONE
                emptyBagMsgLayout.visibility = View.VISIBLE

            }
            else{
                emptyBagMsgLayout.visibility = View.GONE
                bottomCartLayout.visibility = View.VISIBLE
                MybagText.visibility = View.VISIBLE
                animationView.pauseAnimation()
            }
            Item.forEach {
                sum += it.price
            }
            totalPriceBagFrag.text = "₹" + sum

        })

        checkOut_BagPage.setOnClickListener {
            val intent = Intent(context, PaymentMethodActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onItemDeleteClick(product: ProductEntity) {
        cartViewModel.deleteCart(product)
        Toast.makeText(context,"Removed From Bag",Toast.LENGTH_SHORT).show()
    }

    override fun onItemUpdateClick(product: ProductEntity) {
        cartViewModel.updateCart(product)
    }


}