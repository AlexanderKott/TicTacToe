package ru.netology.funnytictactoe

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import ru.netology.funnytictactoe.databinding.ActivityMainBinding


class DrugAndDropHandler(val context: Context,
                         val binding: ActivityMainBinding,
                         val game: Game,
                         val gfh: GameFieldHelper,
                         val resources: Resources) :   View.OnDragListener, View.OnLongClickListener{

    override fun onDrag(v: View?, event: DragEvent?): Boolean {
        val action = event?.action;
        when (action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    v?.getBackground()?.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
                    v?.invalidate();

                    // val sizeInPixel: Int =  resources.getDimensionPixelSize(R.dimen.shapeMax)
                    // binding.OPos.drawable.setBounds(0, 0, sizeInPixel, sizeInPixel );

                    return true;
                }
                return false
            }

            DragEvent.ACTION_DRAG_ENTERED -> {
                v?.getBackground()?.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                v?.invalidate();
                return true;
            }
            DragEvent.ACTION_DRAG_LOCATION ->
                return true;

            DragEvent.ACTION_DRAG_EXITED -> {
                v?.getBackground()?.clearColorFilter();
                v?.invalidate()
                return true;
            }
            DragEvent.ACTION_DROP -> {
                v?.getBackground()?.clearColorFilter();
                v?.invalidate();
                gfh.acceptDrop(event, v)

                val item = event.clipData.getItemAt(0)
                val dragData = item.text.toString()
                if (dragData == "X") {
                    binding.moveOinfo.setAnimation(AnimationUtils.loadAnimation(context, R.anim.blink))
                    binding.moveXInfo.clearAnimation()
                }else {
                    binding.moveXInfo.setAnimation(AnimationUtils.loadAnimation(context, R.anim.blink))
                    binding.moveOinfo.clearAnimation()
                }
                return true
            }

            DragEvent.ACTION_DRAG_ENDED -> {
                v?.getBackground()?.clearColorFilter();
                v?.invalidate();
                if (event.result) {
                    //  val sizeInPixel: Int =  resources.getDimensionPixelSize(R.dimen.shapeNormal)
                    // binding.OPos.drawable.setBounds(0, 0, sizeInPixel, sizeInPixel );

                    gfh.gameLoop(game)
                    spawnNewXandO()
                }
                return true
            }
            else -> {
                Log.e("DragDrop", "Unknown action type received by OnDragListener.");
            }
        }
        return false;
    }

    private fun spawnNewXandO() {
        if (binding.cellOSpawn.childCount == 0) {
            val imgO: ImageView = ImageView(context)
            imgO.setTag("O")
            imgO.setOnLongClickListener(this)
            imgO.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
            imgO.background = resources.getDrawable(R.drawable.button_selector)
            val lp: ViewGroup.LayoutParams = ViewGroup
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)


            binding.cellOSpawn.addView(imgO)
            //imgO.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
            imgO.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_out))
        }

        if (binding.cellXSpawn.childCount == 0) {
            val imgX: ImageView = ImageView(context)
            imgX.setTag("X")
            imgX.setOnLongClickListener(this)
            imgX.setImageResource(R.drawable.ic_baseline_clear_24)
            imgX.background = resources.getDrawable(R.drawable.button_selector)
            val lp: ViewGroup.LayoutParams = ViewGroup
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
          /*  val b = imgX.drawable.bounds
            imgX.drawable.setBounds(0,0,
                    b.right - 100,
                    100)*/
            binding.cellXSpawn.addView(imgX)
            imgX.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_out))
        }
    }




    override fun onLongClick(v: View?): Boolean {
        val item = ClipData.Item(v!!.tag as CharSequence)
        val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
        val data = ClipData(v!!.tag.toString(), mimeTypes, item)



        val dragshadow = View.DragShadowBuilder(v)
        v!!.startDrag(data // data to be dragged
                , dragshadow // drag shadow builder
                , v // local data about the drag and drop operation
                , 0 // flags (not currently used, set to 0)
        )
        return true
    }


}