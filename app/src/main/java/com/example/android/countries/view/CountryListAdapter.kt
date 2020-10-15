package com.example.android.countries.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.countries.R
import com.example.android.countries.model.Country
import com.example.android.countries.util.getProgressDrawable
import com.example.android.countries.util.loadImage
import kotlinx.android.synthetic.main.list_item_country.view.*

class CountryListAdapter(var countries: ArrayList<Country>) :
    RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>() {

    fun updateCountries(newCountries: List<Country>) {
        countries.clear()
        countries.addAll(newCountries)
        notifyDataSetChanged()
    }

    class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val countryName = view.name
        private val imageView = view.imageView
        private val countryCapital = view.capital
        private val progressDrawable = getProgressDrawable(view.context)
        fun bind(country: Country) {
            countryName.text = country.countryName
            countryCapital.text = country.capital
            //image view
            imageView.loadImage(country.flagPNG, progressDrawable)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CountryViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.list_item_country, parent, false)
    )

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }
}