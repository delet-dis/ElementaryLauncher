package com.delet_dis.elementarylauncher.data.mappers

import android.Manifest
import android.appwidget.AppWidgetManager
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.database.EntitiesParent
import com.delet_dis.elementarylauncher.data.database.entities.App
import com.delet_dis.elementarylauncher.data.database.entities.Contact
import com.delet_dis.elementarylauncher.data.database.entities.ContactCall
import com.delet_dis.elementarylauncher.data.database.entities.ContactSMS
import com.delet_dis.elementarylauncher.data.database.entities.SettingsAction
import com.delet_dis.elementarylauncher.data.database.entities.Widget
import com.delet_dis.elementarylauncher.data.models.Card
import com.delet_dis.elementarylauncher.domain.extensions.addPadding
import com.delet_dis.elementarylauncher.domain.extensions.findSettingsAction
import com.delet_dis.elementarylauncher.domain.extensions.getResizedDrawable
import java.io.IOException

/**
 * Casts a persisted shortcut into a display Card without assuming its external target still exists.
 */
fun mapEntityToCard(inputDataClass: EntitiesParent, context: Context): Card {
    val card = Card()
    when (inputDataClass) {
        is App -> mapEntityToApp(context, inputDataClass, card)
        is Contact -> mapEntityToContact(context, inputDataClass, card)
        is ContactCall -> mapEntityToContactCall(context, inputDataClass, card)
        is ContactSMS -> mapEntityToContactSms(context, inputDataClass, card)
        is SettingsAction -> mapEntityToSettingsAction(context, inputDataClass, card)
        is Widget -> mapEntityToWidget(context, inputDataClass, card)
    }
    return card
}

private fun mapEntityToWidget(context: Context, entity: Widget, card: Card) = with(card) {
    position = entity.position
    isWidget = true
    widgetId = entity.widgetId

    val provider = entity.widgetId?.let {
        AppWidgetManager.getInstance(context).getAppWidgetInfo(it)
    } ?: return@with
    val applicationInfo = getApplicationInfo(context, provider.provider.packageName) ?: return@with
    name = applicationInfo.loadLabel(context.packageManager).toString()
    icon = applicationInfo.loadIcon(context.packageManager)
}

private fun mapEntityToSettingsAction(
    context: Context,
    entity: SettingsAction,
    card: Card
) = with(card) {
    val action = entity.actionName?.let(::findSettingsAction)
    name = action?.stringId?.let(context::getString)
    icon = action?.imageId?.let { imageId ->
        ContextCompat.getDrawable(context, imageId)?.toBitmap()
            ?.addPadding(
                50,
                50,
                50,
                50,
                context.getColor(R.color.imageCardBackgroundColor)
            )
            ?.toDrawable(context.resources)
    }
    position = entity.position
    onClickAction = {
        entity.actionName?.let { safeStartActivity(context, Intent(it)) }
    }
}

private fun mapEntityToContactSms(context: Context, entity: ContactSMS, card: Card) {
    val uri = Uri.parse(entity.contactURI)
    val fetchedName = getContactName(uri, context)
    applyContactPresentation(context, card, uri, fetchedName, entity.position)
    card.name = context.getString(R.string.actionSMSPrefix) +
        (fetchedName ?: context.getString(R.string.contactUnavailable))
    card.onClickAction = {
        getContactPhoneNumber(uri, context)?.let { phone ->
            safeStartActivity(context, Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:$phone")))
        }
    }
}

private fun mapEntityToContactCall(context: Context, entity: ContactCall, card: Card) {
    val uri = Uri.parse(entity.contactURI)
    val fetchedName = getContactName(uri, context)
    applyContactPresentation(context, card, uri, fetchedName, entity.position)
    card.name = context.getString(R.string.actionCallPrefix) +
        (fetchedName ?: context.getString(R.string.contactUnavailable))
    card.onClickAction = {
        getContactPhoneNumber(uri, context)?.let { phone ->
            launchPhoneCall(context, phone)
        }
    }
}

private fun mapEntityToContact(context: Context, entity: Contact, card: Card) {
    val uri = Uri.parse(entity.contactURI)
    val fetchedName = getContactName(uri, context)
    applyContactPresentation(context, card, uri, fetchedName, entity.position)
    card.name = fetchedName ?: context.getString(R.string.contactUnavailable)
    card.onClickAction = {
        if (hasContactPermission(context)) {
            safeStartActivity(context, Intent(Intent.ACTION_VIEW, uri))
        }
    }
}

private fun applyContactPresentation(
    context: Context,
    card: Card,
    uri: Uri,
    name: String?,
    position: Int?
) = with(card) {
    text = getCardTextBasedOnName(name)
    getContactPhoto(uri, context)?.let { bitmap ->
        icon = bitmap.toDrawable(context.resources).getResizedDrawable(2f)
    }
    this.position = position
}

private fun mapEntityToApp(context: Context, entity: App, card: Card) {
    val applicationInfo = entity.packageName?.let { getApplicationInfo(context, it) }
    with(card) {
        name = displayNameOrPlaceholder(
            applicationInfo?.loadLabel(context.packageManager),
            context.getString(R.string.appUnavailable)
        )
        icon = applicationInfo?.loadIcon(context.packageManager)
            ?: ContextCompat.getDrawable(context, R.drawable.ic_app)
        position = entity.position
        onClickAction = {
            entity.packageName
                ?.let(context.packageManager::getLaunchIntentForPackage)
                ?.let { safeStartActivity(context, it) }
        }
    }
}

internal fun displayNameOrPlaceholder(label: CharSequence?, placeholder: String): String =
    label?.toString()?.takeIf(String::isNotBlank) ?: placeholder

private fun getApplicationInfo(context: Context, packageName: String) =
    try {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            context.packageManager.getApplicationInfo(
                packageName,
                PackageManager.ApplicationInfoFlags.of(0)
            )
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getApplicationInfo(packageName, 0)
        }
    } catch (_: PackageManager.NameNotFoundException) {
        null
    }

internal fun chooseCallAction(hasCallPermission: Boolean, directCallAvailable: Boolean): String =
    if (hasCallPermission && directCallAvailable) Intent.ACTION_CALL else Intent.ACTION_DIAL

private fun launchPhoneCall(context: Context, phone: String) {
    val uri = Uri.parse("tel:$phone")
    val hasPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CALL_PHONE
    ) == PackageManager.PERMISSION_GRANTED
    val directCallAvailable = context.packageManager.hasSystemFeature(
        PackageManager.FEATURE_TELEPHONY
    )
    val action = chooseCallAction(hasPermission, directCallAvailable)

    if (!safeStartActivity(context, Intent(action, uri)) && action == Intent.ACTION_CALL) {
        safeStartActivity(context, Intent(Intent.ACTION_DIAL, uri))
    }
}

private fun safeStartActivity(context: Context, intent: Intent): Boolean {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return try {
        context.startActivity(intent)
        true
    } catch (_: ActivityNotFoundException) {
        // External targets can disappear at any time; the card remains as a safe placeholder.
        false
    } catch (_: SecurityException) {
        // Permissions and exported state can also change after the card was persisted.
        false
    }
}

private fun getCardTextBasedOnName(input: String?): String = input
    ?.take(2)
    .orEmpty()

private fun hasContactPermission(context: Context): Boolean =
    ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED

private fun getContactName(contactUri: Uri, context: Context): String? {
    if (!hasContactPermission(context)) return null
    return try {
        context.contentResolver.query(
            contactUri,
            arrayOf(ContactsContract.Contacts.DISPLAY_NAME),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getString(0) else null
        }
    } catch (_: SecurityException) {
        null
    }
}

private fun getContactPhoneNumber(contactUri: Uri, context: Context): String? {
    if (!hasContactPermission(context)) return null
    return try {
        val contactId = getContactId(contactUri, context) ?: return null
        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use { numbers ->
            if (numbers.moveToFirst()) numbers.getString(0) else null
        }
    } catch (_: SecurityException) {
        null
    }
}

private fun getContactId(contactUri: Uri, context: Context): Long? {
    if (!hasContactPermission(context)) return null
    return try {
        context.contentResolver.query(
            contactUri,
            arrayOf(ContactsContract.Contacts._ID),
            null,
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) cursor.getLong(0) else null
        }
    } catch (_: SecurityException) {
        null
    }
}

private fun getContactPhoto(contactUri: Uri, context: Context): Bitmap? {
    if (!hasContactPermission(context)) return null
    return try {
        val contactId = getContactId(contactUri, context) ?: return null
        ContactsContract.Contacts.openContactPhotoInputStream(
            context.contentResolver,
            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId)
        )?.use(BitmapFactory::decodeStream)
    } catch (_: IOException) {
        null
    } catch (_: SecurityException) {
        null
    }
}
