package com.example.voicerecorderapp

interface onItemClickListener {
    // tells which item (audio) in the recycler should be played when clicked
    fun onItemClickListener(position: Int) // gets the item (audio) in the recycler view
    fun onItemLongClickListener(position: Int) // gets the item (audio) in the recycler view
}