package com.example.myapp

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class DemoActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        // UI widgets button and
        val bOpenAlertDialog = findViewById<Button>(R.id.openAlertDialogButton)
        val tvSelectedItemsPreview = findViewById<TextView>(R.id.selectedItemPreview)

        // initialise the list items for the alert dialog
        val listItems = arrayOf("Cricket", "Dancing", "Listening Music", "Reading", "Singing", "Traveling")
        val checkedItems = BooleanArray(listItems.size)

        // copy the items from the main list to the selected item list for the preview
        // if the item is checked then only the item should be displayed for the user
        val selectedItems = mutableListOf(*listItems)

        // handle the Open Alert Dialog button
        bOpenAlertDialog.setOnClickListener {
            // initially set the null for the text preview
            tvSelectedItemsPreview.text = null

            // initialise the alert dialog builder
            val builder = AlertDialog.Builder(this)

            // set the title for the alert dialog
            builder.setTitle("Choose Items")


            // now this is the function which sets the alert dialog for multiple item selection ready
            builder.setMultiChoiceItems(listItems, checkedItems) { dialog, which, isChecked ->
                checkedItems[which] = isChecked
                val currentItem = selectedItems[which]
            }

            // alert dialog shouldn't be cancellable
            builder.setCancelable(false)

            // handle the positive button of the dialog
            builder.setPositiveButton("Done") { dialog, which ->
                for (i in checkedItems.indices) {
                    if (checkedItems[i]) {
                        tvSelectedItemsPreview.text = String.format("%s%s, ", tvSelectedItemsPreview.text, selectedItems[i])
                    }
                }
            }

            // handle the negative button of the alert dialog
            builder.setNegativeButton("CANCEL") { dialog, which -> }

            // handle the neutral button of the dialog to clear the selected items boolean checkedItem
            builder.setNeutralButton("CLEAR ALL") { dialog: DialogInterface?, which: Int ->
                Arrays.fill(checkedItems, false)
            }

            // create the builder
            builder.create()

            // create the alert dialog with the alert dialog builder instance
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }
}
