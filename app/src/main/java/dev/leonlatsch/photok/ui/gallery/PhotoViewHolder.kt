package dev.leonlatsch.photok.ui.gallery

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import dev.leonlatsch.photok.R
import dev.leonlatsch.photok.model.database.entity.Photo
import dev.leonlatsch.photok.model.repositories.PhotoRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1

class PhotoViewHolder(
    parent: ViewGroup,
    private val context: Context,
    private val photoRepository: PhotoRepository
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
) {
    private val imageView: ImageView = itemView.findViewById(R.id.photoItemImageView)
    var photo: Photo? = null

    fun bindTo(onClickCallback: KFunction1<Int, Unit>, photo: Photo?) {
        this.photo = photo
        loadThumbnail()
        imageView.setOnClickListener {
            onClickCallback.invoke(photo?.id!!)
        }
    }

    private fun loadThumbnail() {
        GlobalScope.launch {
            val thumbnailBytes = photoRepository.readPhotoThumbnailData(context, photo?.id!!)
            val thumbnailBitmap = BitmapFactory.decodeByteArray(thumbnailBytes, 0, thumbnailBytes.size)
            Handler(context.mainLooper).post {
                imageView.setImageBitmap(thumbnailBitmap)
            }
        }
    }
}