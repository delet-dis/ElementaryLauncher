package com.delet_dis.elementarylauncher.common.mappers

import android.appwidget.AppWidgetManager
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.common.extensions.addPadding
import com.delet_dis.elementarylauncher.common.extensions.findSettingsAction
import com.delet_dis.elementarylauncher.common.extensions.getResizedDrawable
import com.delet_dis.elementarylauncher.common.models.Card
import com.delet_dis.elementarylauncher.data.database.entities.*
import java.io.IOException


fun mapEntityToCard(inputDataClass: Any, context: Context): Card {
    val cardToReturn = Card()

    when (inputDataClass) {
        is App -> {
            val packageManager = context.packageManager

            val processingApplicationInfo =
                inputDataClass.packageName?.let {
                    packageManager.getApplicationInfo(it, 0)
                }

            with(cardToReturn) {
                name = processingApplicationInfo?.loadLabel(packageManager).toString()
                icon = processingApplicationInfo?.loadIcon(packageManager)
                position = inputDataClass.position

                onClickAction = {
                    context.startActivity(inputDataClass.packageName?.let {
                        packageManager.getLaunchIntentForPackage(
                            it
                        )
                    })
                }
            }
        }

        is Contact -> {
            val uri: Uri = Uri.parse(inputDataClass.contactURI)

            with(cardToReturn) {
                val fetchedName = getContactName(
                    uri,
                    context
                )

                name = fetchedName

                text = fetchedName?.subSequence(0, 2).toString()

                getContactPhoto(uri, context)?.let {
                    icon = it.toDrawable(context.resources).getResizedDrawable(2f)
                }

                position = inputDataClass.position

                onClickAction = {
                    val contactCardIntent = Intent(Intent.ACTION_VIEW)
                    contactCardIntent.data = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_URI,
                        getContactId(uri, context).toString()
                    )
                    contactCardIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(contactCardIntent)
                }
            }
        }

        is ContactCall -> {
            val uri: Uri = Uri.parse(inputDataClass.contactURI)

            with(cardToReturn) {
                val fetchedName = getContactName(
                    uri,
                    context
                )

                name = context.getString(R.string.actionCallPrefix) + fetchedName

                text = fetchedName?.subSequence(0, 2).toString()

                getContactPhoto(uri, context)?.let {
                    icon = it.toDrawable(context.resources).getResizedDrawable(2f)
                }

                position = inputDataClass.position

                onClickAction = {
                    val phoneToCall = getContactPhoneNumber(uri, context)

                    val callIntent = Intent(Intent.ACTION_CALL)
                    callIntent.data = Uri.parse("tel:$phoneToCall")
                    callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(callIntent)
                }
            }
        }

        is ContactSMS -> {
            val uri: Uri = Uri.parse(inputDataClass.contactURI)

            with(cardToReturn) {
                val fetchedName = getContactName(
                    uri,
                    context
                )

                name =
                    context.getString(R.string.actionSMSPrefix) + fetchedName

                text = fetchedName?.subSequence(0, 2).toString()

                getContactPhoto(uri, context)?.let {
                    icon = it.toDrawable(context.resources).getResizedDrawable(2f)
                }

                position = inputDataClass.position

                onClickAction = {
                    val phoneToCall = getContactPhoneNumber(uri, context)

                    val callIntent = Intent(Intent.ACTION_VIEW)
                    callIntent.data = Uri.parse("sms:$phoneToCall")
                    callIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(callIntent)
                }
            }
        }

        is SettingsAction -> {
            with(cardToReturn) {
                with(inputDataClass.actionName?.let { findSettingsAction(it) }) {
                    name = this?.stringId?.let { context.getString(it) }

                    icon = this?.imageId?.let {
                        ContextCompat.getDrawable(context, it)?.toBitmap()
                            ?.addPadding(50, 50, 50, 50, Color.WHITE)
                            ?.toDrawable(context.resources)
                    }

                    position = inputDataClass.position

                    onClickAction = {
                        context.startActivity(
                            Intent(inputDataClass.actionName)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    }
                }
            }
        }

        is Shortcut -> {
            val packageManager = context.packageManager

            val processingApplicationInfo =
                inputDataClass.packageName?.let {
                    packageManager.getApplicationInfo(it, 0)
                }

            with(cardToReturn) {
                name = processingApplicationInfo?.loadLabel(packageManager).toString()
                icon = processingApplicationInfo?.loadIcon(packageManager)
                position = inputDataClass.position
            }
        }

        is Widget -> {
            val packageManager = context.packageManager

            val appWidgetProviderInfo =
                inputDataClass.widgetId?.let {
                    AppWidgetManager.getInstance(context).getAppWidgetInfo(
                        it
                    )
                }

            appWidgetProviderInfo?.let {
                val processingApplicationInfo =
                    packageManager.getApplicationInfo(it.provider.packageName, 0)

                with(cardToReturn) {
                    name = processingApplicationInfo.loadLabel(packageManager).toString()
                    icon = processingApplicationInfo.loadIcon(packageManager)
                    position = inputDataClass.position
                    isWidget = true
                    widgetId = inputDataClass.widgetId
                }
            }
        }
    }

    return cardToReturn
}

private fun getContactName(contactUri: Uri, context: Context): String? {
    var contactName: String? = null

    val cursor: Cursor? = context.contentResolver
        .query(contactUri, null, null, null, null)

    cursor?.let {
        if (it.moveToFirst()) {
            contactName =
                it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
        }

        cursor.close()
    }

    return contactName
}

private fun getContactPhoneNumber(contactUri: Uri, context: Context): String? {
    var contactPhone: String? = null

    val cursor: Cursor? = context.contentResolver
        .query(contactUri, null, null, null, null)

    cursor?.let {
        if (it.moveToFirst()) {

            val contactId: String = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
            val hasNumber: String =
                it.getString(it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

            if (hasNumber.toInt() == 1) {
                val numbers: Cursor = context.contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null
                )!!
                while (numbers.moveToNext()) {
                    contactPhone =
                        numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                }
                numbers.close()
            }
        }

        cursor.close()
    }

    return contactPhone
}

private fun getContactId(contactUri: Uri, context: Context): Long? {
    val cursor: Cursor? = context.contentResolver
        .query(contactUri, null, null, null, null)

    var id: String? = null

    cursor?.let {
        if (it.moveToFirst()) {
            id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
        }
        cursor.close()
    }

    return id?.toLong()
}


private fun getContactPhoto(contactUri: Uri, context: Context): Bitmap? {
    var photo: Bitmap? = null
    try {
        val inputStream = ContactsContract.Contacts.openContactPhotoInputStream(
            context.contentResolver,
            getContactId(contactUri, context)?.let {
                ContentUris.withAppendedId(
                    ContactsContract.Contacts.CONTENT_URI,
                    it
                )
            }
        )
        if (inputStream != null) {
            photo = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return photo
}