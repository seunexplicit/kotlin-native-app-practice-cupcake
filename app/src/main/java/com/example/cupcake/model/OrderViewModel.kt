package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_PER_CUPCAKE = 2.0
private const val PRICE_FOR_FIRST_DAY_PICKUP = 3.00

class OrderViewModel:ViewModel() {

    private val _quantity = MutableLiveData<Int>()
    val quantity:LiveData<Int> = _quantity

    private val _flavour = MutableLiveData<String>()
    val flavour:LiveData<String> = _flavour

    private val _date = MutableLiveData<String>()
    val date:LiveData<String> = _date

    private val _price = MutableLiveData<Double>()
    val price:LiveData<String> = Transformations.map(_price){
        NumberFormat.getCurrencyInstance().format(it)
    }

    val dateOptions = getPickUpOptions()

    init {
        resetOrder()
    }

    fun setQuantity(quantity:Int){
        _quantity.value = quantity
        updatePrice()
    }

    private fun updatePrice(){
        var calculatedPrice = (_quantity.value?:0)* PRICE_PER_CUPCAKE
        if(_date.value===dateOptions[0]){
            calculatedPrice+= PRICE_FOR_FIRST_DAY_PICKUP
        }
        _price.value = calculatedPrice
    }

    fun setDate(date:String){
        _date.value = date
        updatePrice()
    }

    fun setFlavour(flavour:String){
        _flavour.value = flavour
    }

    fun hasNoFlavourSet():Boolean{
        return _flavour.value.isNullOrEmpty()
    }

    private fun getPickUpOptions():List<String>{
        val options = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()

        repeat(4){
            options.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }

        return options
    }

    fun resetOrder(){
        _flavour.value = ""
        _date.value = dateOptions[0]
        _quantity.value = 0
        _price.value = 0.0
    }

}