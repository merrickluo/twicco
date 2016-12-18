package ninja.luois.twicco.timeline.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import kotterknife.bindView
import ninja.luois.twicco.R
import ninja.luois.twicco.common.DialogFragment
import ninja.luois.twicco.extension.ui.getScreenSize
import ninja.luois.twicco.timeline.viewmodel.Media


class MediaPreviewDialog(val medias: List<Media>, val position: Int = 0) : DialogFragment() {

    val counterView: TextView by bindView(R.id.text_media_counter)
    val pager: ViewPager by bindView(R.id.view_pager_media)

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.dialog_media_preview, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // the content
        val root = RelativeLayout(activity)
        root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        root.setBackgroundColor(Color.TRANSPARENT)

        // creating the fullscreen dialog
        val dialog = Dialog(activity)
        dialog.setContentView(root)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)

        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pager.adapter = MediaViewPagerAdapter(activity, medias) {
            dismiss()
        }
        if (medias.size > 1) {
            counterView.text = "1 of ${medias.size}"
        }
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                counterView.text = "${position+1} of ${medias.size}"
            }

        })
        pager.currentItem = position
    }
}

class MediaViewPagerAdapter(val ctx: Context,
                            val medias: List<Media>,
                            val clickAction: () -> Unit) : PagerAdapter() {

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object` as ImageView
    }

    override fun getCount(): Int {
        return medias.size
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {

        val padding = ctx.resources.getDimensionPixelSize(
                R.dimen.padding_medium)

        val imageView = SimpleDraweeView(ctx)
        imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)

        imageView.setPadding(padding, padding, padding, padding)
        imageView.setBackgroundColor(Color.TRANSPARENT)

        imageView.hierarchy = GenericDraweeHierarchyBuilder(ctx.resources)
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .build()

        val m = medias[position]

        val size = ctx.getScreenSize()
        val origReq = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse("${m.url}:orig"))
                .setResizeOptions(ResizeOptions(size.x, size.y))
                .build()

        imageView.controller = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(ImageRequest.fromUri(Uri.parse(m.url)))
                .setImageRequest(origReq)
                .setOldController(imageView.controller)
                .build()

        imageView.setOnClickListener { clickAction() }

        (container as ViewPager).addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        (container as? ViewPager)?.removeView(`object` as ImageView)
    }
}
