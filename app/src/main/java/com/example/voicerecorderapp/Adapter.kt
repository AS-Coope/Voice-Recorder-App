package com.example.voicerecorderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.voicerecorderapp.audiodatabase.Audio

class Adapter(
    var audioFiles: ArrayList<Audio>, var itemListener: onItemClickListener
    ): RecyclerView.Adapter<Adapter.ViewHolder>() {

    // , View.OnLongClickListener - add to the ViewHolder class implements list when using longClickListener
    inner class ViewHolder(theView: View): RecyclerView.ViewHolder(theView), View.OnClickListener{
        var audioName: TextView = theView.findViewById(R.id.audioName)
        var audioSize: TextView = theView.findViewById(R.id.audioSize)
        var audioLength: TextView = theView.findViewById(R.id.audioLength)

        init {
            itemView.setOnClickListener(this)

            // uncomment when making onLongClickListener
            //itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {

            // get the position of the item in the recycler view
            val itemPosition = adapterPosition
            // ensuring that the item does appear in the recycler view
            if (itemPosition != RecyclerView.NO_POSITION) {
                itemListener.onItemClickListener(itemPosition)
            }
        }

        /*
        override fun onLongClick(v: View?): Boolean {
            // getting the position of the item in the recycler view
            val itemPosition = adapterPosition
            // ensuring that the item does appear in the recycler view (that it is not out of bounds)
            if (itemPosition != RecyclerView.NO_POSITION){
                itemListener.onItemLongClickListener(itemPosition)
                return true
            }
            return false
        }

         */


    }// end of View Holder inner class

    // creates the audio layout but does not fill in the various text fields
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val startView = LayoutInflater.from(parent.context).inflate(R.layout.audio_layout, parent, false)
        return ViewHolder(startView)
    }

    // getting number of items in the audioRecords array
    override fun getItemCount(): Int {
        return audioFiles.size
    }

    // here is where the data is now decided when to be shown on screen/ filled in its relevant text fields, respectively
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position!= RecyclerView.NO_POSITION){
            val record = audioFiles[position]
            holder.audioName.text = record.aName
            holder.audioSize.text = record.audioSize
            holder.audioLength.text = record.aLength
        }
    }
}