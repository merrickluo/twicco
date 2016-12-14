package ninja.luois.twicco.compose.view

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import com.jakewharton.rxbinding.view.clicks
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import kotterknife.bindView
import ninja.luois.twicco.R
import ninja.luois.twicco.common.DialogFragment
import ninja.luois.twicco.compose.provider.Media
import java.io.File


class PreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val imageView by bindView<ImageView>(R.id.image_preview)
    val clearButton by bindView<ImageButton>(R.id.button_clear)
}

class PreviewImageAdapter(val ctx: Context,
                          var medias: List<Media>,
                          val clickAction: (Int) -> Unit,
                          val emptyAction: () -> Unit) :
        RecyclerView.Adapter<PreviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PreviewViewHolder {
        val itemView = LayoutInflater.from(ctx)
                .inflate(R.layout.item_image_preview, parent, false)
        return PreviewViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return medias.size
    }

    override fun onBindViewHolder(holder: PreviewViewHolder?, position: Int) {
        holder?.imageView?.let {
            Picasso.with(ctx)
                    .load(File(medias[position].filepath))
                    .resizeDimen(R.dimen.new_tweet_preview_image_size,
                            R.dimen.new_tweet_preview_image_size)
                    .centerCrop()
                    .into(it)
        }
        holder?.clearButton?.setOnClickListener {
            medias = medias.filterIndexed { i, media -> i != position }
            notifyDataSetChanged()
            clickAction(position)
            if (medias.isEmpty()) {
                emptyAction()
            }
        }
    }
}

class ImagePreviewDialog(private val medias: List<Media>,
                         private val clearAction: (Int) -> Unit,
                         private val cameraAction: () -> Unit,
                         private val galleryAction: () -> Unit) : DialogFragment() {

    private val cameraButton by bindView<View>(R.id.button_camera)
    private val galleryButton by bindView<View>(R.id.button_gallery)
    private val previewGridView by bindView<RecyclerView>(R.id.image_preview_grid)

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_image_preview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (medias.isEmpty()) {
            previewGridView.visibility = View.GONE
        } else {
            previewGridView.visibility = View.VISIBLE
            previewGridView.layoutManager =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

            previewGridView.adapter = PreviewImageAdapter(activity, medias, {
                clearAction(it)
            }, {
                previewGridView.visibility = View.GONE
            })
        }

        cameraButton.clicks()
                .bindToLifecycle(this)
                .subscribe {
                    cameraAction()
                    dismiss()
                }

        galleryButton.clicks()
                .bindToLifecycle(this)
                .subscribe {
                    galleryAction()
                    dismiss()
                }
    }

}
