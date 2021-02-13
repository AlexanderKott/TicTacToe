package ru.netology.funnytictactoe.view

import android.content.res.Resources
import android.graphics.Point
import android.text.Html
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import ru.netology.funnytictactoe.R
import ru.netology.funnytictactoe.databinding.ActivityMainBinding
import ru.netology.funnytictactoe.logic.GameResult
import ru.netology.funnytictactoe.logic.GameState
import ru.netology.funnytictactoe.logic.computerMove

class GameFieldHelper(
    val binding: ActivityMainBinding,
    val gameState: GameState,
    private val resources: Resources
) {


    private val fields = arrayOf(
        binding.cell0, binding.cell1, binding.cell2, binding.cell3,
        binding.cell4, binding.cell5, binding.cell6, binding.cell7,
        binding.cell8
    )

      fun displayInfo(message: String) {
        binding.gameInfo
            .text = Html.fromHtml(
            "<font color=\"blue\">X score:${gameState.xScores}</font>" +
                    "&nbsp;&nbsp;&nbsp;<b>$message</b>&nbsp;&nbsp;&nbsp;<font color=\"red\">O score:" +
                    "${gameState.oScores}</font>"
        )
    }

    fun playRowDisappearAnimationAndClearField(
        gameResult: GameResult,
        lastAction: () -> Unit
    ) {
        val animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.scale_in);

        startRowAnimation(gameResult, animation)

        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                clearGameField()
                lastAction()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }

    private fun startRowAnimation(
        gameResult: GameResult,
        animation: Animation?
    ) {
        for (f in fields) {
            for (item in gameResult.WinnersRow)
                if ((f.tag as OTag).number == item) {
                    if (f.getChildAt(0) != null) {
                        f.getChildAt(0).background =
                            resources.getDrawable(R.drawable.shadow_red)
                        f.getChildAt(0).startAnimation(animation)
                    }
                }
        }
    }


    fun spawn(layout: ViewGroup, resource: Int, sign: String, dndHelper: View.OnLongClickListener?) {
        if (layout.childCount == 0) {
            val img: ImageView = ImageView(binding.root.context)
            img.tag = sign
            img.setOnLongClickListener(dndHelper)
            img.setImageResource(resource)
            img.background = resources.getDrawable(R.drawable.sign_selector)
            val lp: ViewGroup.LayoutParams = ViewGroup
                .LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

            layout.addView(img)
            img.visibility = View.VISIBLE
            img.animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.scale_out)
        }
    }

    fun spawnComputerIcon() {
        val img: ImageView = ImageView(binding.root.context)
        img.setImageResource(R.drawable.computer)
        img.background = resources.getDrawable(R.drawable.shadow_yellow)
        binding.cellOSpawn.addView(img)
        img.visibility = View.VISIBLE
        img.animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.scale_out)

    }

    /**
     * move cells data from UI to array
     */
      fun updateDataFromFieldtoArray(gameState: GameState) {
        for (f in fields) {
            if (f.getChildAt(0) != null) {
                gameState.gameField[(f.tag as OTag).point.x][(f.tag as OTag).point.y] =
                    (f.getChildAt(0) as ImageView).tag.toString()
                (f.getChildAt(0) as ImageView).setOnLongClickListener(null)
            } else {
                gameState.gameField[(f.tag as OTag).point.x][(f.tag as OTag).point.y] = "_"
            }
        }
        binding.info.text = gameState.gameField.contentDeepToString()

    }

    fun addComputerMoveToGameField() {
        val computerMove: Array<Int> = computerMove(gameState)

        for (f in fields) {
            if ((f.tag as OTag).point.x == computerMove[0] &&
                (f.tag as OTag).point.y == computerMove[1]
            ) {
                spawn(f, R.drawable.o_sign, "O", null)
                break
            }
        }
    }

    fun clearGameField() {
        for (f in fields) {
            f.removeAllViews()
        }
    }

    /**Add X or O to a cell
     *
     */
    fun acceptDrop(event: DragEvent?, v: View?) {
        val vw: View = event?.localState as View;
        val owner: ViewGroup = vw.parent as ViewGroup
        owner.removeView(vw);
        val container: LinearLayout = v as LinearLayout;
        if (container.getChildAt(0) != null) {
            gameState.isCheating = true
        }
        container.removeAllViews()
        container.addView(vw)
        vw.visibility = View.VISIBLE
    }

    fun initFieldLayout(binding: ActivityMainBinding, dndHelper: View.OnDragListener) {
        binding.cell0.setOnDragListener(dndHelper)
        binding.cell0.tag = OTag(0, Point(0, 0))

        binding.cell1.setOnDragListener(dndHelper)
        binding.cell1.tag = OTag(1, Point(0, 1))

        binding.cell2.setOnDragListener(dndHelper)
        binding.cell2.tag = OTag(2, Point(0, 2))

        binding.cell3.setOnDragListener(dndHelper)
        binding.cell3.tag = OTag(3, Point(1, 0))

        binding.cell4.setOnDragListener(dndHelper)
        binding.cell4.tag = OTag(4, Point(1, 1))

        binding.cell5.setOnDragListener(dndHelper)
        binding.cell5.tag = OTag(5, Point(1, 2))

        binding.cell6.setOnDragListener(dndHelper)
        binding.cell6.tag = OTag(6, Point(2, 0))

        binding.cell7.setOnDragListener(dndHelper)
        binding.cell7.tag = OTag(7, Point(2, 1))

        binding.cell8.setOnDragListener(dndHelper)
        binding.cell8.tag = OTag(8, Point(2, 2))
    }
}