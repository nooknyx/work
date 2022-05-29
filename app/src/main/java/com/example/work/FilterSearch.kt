package com.example.work

import android.widget.Filter
import com.example.work.Adapter.BookAdapter
import com.example.work.data.Bookdata

class FilterSearch : Filter {

    var filterList: ArrayList<Bookdata>
    var bookAdapter: BookAdapter

    constructor(filterList: ArrayList<Bookdata>, bookAdapter: BookAdapter) : super() {
        this.filterList = filterList
        this.bookAdapter = bookAdapter
    }

    override fun performFiltering(constraint: CharSequence): FilterResults {
        //value to search
        var constraint: CharSequence? = constraint
        val results = FilterResults()
        //valur to be search should not be null and not empty
        if (constraint != null && constraint.isNotEmpty()){
            //not null nor empty

            //change to upper case, or lower case to remove case sensitivity
            constraint = constraint.toString().uppercase()
            val filteredBook = ArrayList<Bookdata>()
            for( i in filterList.indices){
                if(filterList[i].name!!.uppercase().contains(constraint)){
                    //search value matched with title, add to list
                    filteredBook.add(filterList[i])
                }
                if(filterList[i].author!!.uppercase().contains(constraint)){
                    //search value matched with title, add to list
                    filteredBook.add(filterList[i])
                }
            }
            //return filtered list and size
            results.count = filteredBook.size
            results.values = filteredBook
        }
        else{
            //either it is null or empty
            //return original list and size
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        //apply filter changes
        bookAdapter.bookdatalist = results.values as ArrayList<Bookdata>

        //notify changes
        bookAdapter.notifyDataSetChanged()
    }

}