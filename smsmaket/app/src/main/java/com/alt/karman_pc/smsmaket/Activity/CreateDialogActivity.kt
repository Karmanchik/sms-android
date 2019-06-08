package com.alt.karman_pc.smsmaket.Activity

import android.app.Activity
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alt.karman_pc.smsmaket.R
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.Contacts.*
import android.support.v7.app.AlertDialog
import android.util.Log
import com.alt.karman_pc.smsmaket.helperFiles.*

class CreateDialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_dialog)
        try {
            //to select phone to create dialog
            val intent = Intent(Intent.ACTION_PICK, CONTENT_URI)
            startActivityForResult(intent, 1)
        } catch (e: Exception) {
            Log.e(TAG, "Error CreateDialogActivity onCreate: ${e.message}")
        }
    }

    private fun loadAllPhones(contact: DataContact): Array<String> {
        var result = emptyArray<String>()

        try {
            if (contact.hasPhones) {
                val cursor = contentResolver.query(
                    Phone.CONTENT_URI,
                    null,
                    Phone.CONTACT_ID + " = ?",
                    arrayOf(contact.id),
                    null
                )

                while (cursor.moveToNext()) {
                    val phoneType = cursor.getInt(cursor.getColumnIndex(Phone.TYPE))
                    val phoneNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER))
                    when (phoneType) {
                        Phone.TYPE_MOBILE ->
                            result += phoneNumber
                        Phone.TYPE_HOME ->
                            result += phoneNumber
                        Phone.TYPE_WORK ->
                            result += phoneNumber
                        Phone.TYPE_OTHER ->
                            result += phoneNumber
                    }
                }
                cursor.close()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error CreateDialogActivity loadAllPhones: ${e.message}")
        }
        return result
    }

    private fun loadContactInfo(uri: Uri): DataContact {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val result = DataContact("", "", "", false)

        if (cursor.moveToFirst()) {
            result.avatar = try {
                cursor.getString(cursor.getColumnIndex(PHOTO_URI))
            } catch (e: Exception) {
                "none"
            }
            result.name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME))
            result.id = cursor.getString(cursor.getColumnIndex(_ID))
            val hasPhonesColumn = cursor.getColumnIndex(HAS_PHONE_NUMBER)
            result.hasPhones = cursor.getString(hasPhonesColumn).toInt() > 0
        }
        cursor.close()
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        try {
            if (resultCode == Activity.RESULT_OK) {

                val contact = loadContactInfo(data.data)
                val phones = loadAllPhones(contact)
                var phone: String

                if (phones.size > 1) {
                    val builder = AlertDialog.Builder(this)
                        .setTitle(R.string.select_phone)
                        .setCancelable(false)
                        .setSingleChoiceItems(phones, -1) { dialog: DialogInterface, i: Int ->
                            phone = phones[i]
                            dialog.dismiss()
                            val intent = Intent(this, DialogActivity::class.java)
                            intent.putExtra("name", contact.name)
                            intent.putExtra("avatar", contact.avatar)
                            intent.putExtra("address", phone)
                            startActivity(intent)
                        }
                    builder.create().show()
                } else {
                    if (phones.isEmpty())
                        finish()
                    phone = phones[0]
                    val intent = Intent(this, DialogActivity::class.java)
                    intent.putExtra("name", contact.name)
                    intent.putExtra("avatar", contact.avatar)
                    intent.putExtra("address", phone)
                    startActivity(intent)
                }

            }
        } catch (e: Exception) {
            Log.e(TAG, "Error CreateDialogActivity onActivityResult: ${e.message}")
        }
    }

    //class for refactor
    class DataContact(var name: String, var avatar: String, var id: String, var hasPhones: Boolean)
}
