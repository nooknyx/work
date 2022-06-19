package com.example.work

import android.widget.Filter
import com.example.work.Adapter.BookAdapter
import com.example.work.data.Bookdata
import com.example.work.ui.Search

class FilterSearch : Filter {

    private var filterList: ArrayList<Bookdata>//arraylist that want to search
    private var bookAdapter: BookAdapter//adapter which filter need to implement

    constructor(filterList: ArrayList<Bookdata>, bookAdapter: BookAdapter) : super() {
        this.filterList = filterList
        this.bookAdapter = bookAdapter
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        //value to search
        var constraint = constraint
        val results = FilterResults()
        //value to be search should not be null and not empty
        if (constraint != null && constraint.isNotEmpty()){//not null or empty
            constraint = constraint.toString().uppercase()//change to upper case, or lower case to remove case sensitivity
            val filteredBook:ArrayList<Bookdata> = ArrayList()
            for( i in 0 until filterList.size){
                //validate
                if(filterList[i].BookTitle!!.uppercase().contains(constraint)&&filterList[i].Author!!.uppercase().contains(constraint)){ //26.48 Part4
                    filteredBook.add(filterList[i])//search value matched with title, add to list
                }
                if(filterList[i].Barcode!!.toString().contains(constraint)){
                    //search value matched with Barcode value, add to list
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