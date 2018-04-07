package com.annasedykh.moneytracker.balance;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.annasedykh.moneytracker.R;

public class DiagramView extends View {

    private int income;
    private int expense;

    private Paint incomePaint = new Paint();
    private Paint expensePaint = new Paint();

    public DiagramView(Context context) {
        this(context, null);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        expensePaint.setColor(getResources().getColor(R.color.balance_expense_color));
        incomePaint.setColor(getResources().getColor(R.color.balance_income_color));

        if (isInEditMode()) {
            expense = 4500;
            income = 19000;
        }
    }

    public void update(int income, int expense) {
        this.expense = expense;
        this.income = income;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            Toast.makeText(getContext(), getContext().getString(R.string.balance_title), Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawPieDiagram(canvas);
        } else {
            drawRectDiagram(canvas);
        }
    }

    private void drawRectDiagram(Canvas canvas) {
        if (expense + income == 0)
            return;

        long max = Math.max(expense, income);
        long expensesHeight = canvas.getHeight() * expense / max;
        long incomeHeight = canvas.getHeight() * income / max;

        int w = getWidth() / 4;

        canvas.drawRect(w / 2, canvas.getHeight() - expensesHeight, w * 3 / 2, canvas.getHeight(), expensePaint);
        canvas.drawRect(5 * w / 2, canvas.getHeight() - incomeHeight, w * 7 / 2, canvas.getHeight(), incomePaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawPieDiagram(Canvas canvas) {
        if (expense + income == 0)
            return;

        float expenseAngle = 360.f * expense / (expense + income);
        float incomeAngle = 360.f * income / (expense + income);

        int space = 10; // space between income and expense
        int size = Math.min(getWidth(), getHeight()) - space * 2;
        final int xMargin = (getWidth() - size) / 2, yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space, getHeight() - yMargin, 180 - expenseAngle / 2, expenseAngle, true, expensePaint);
        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space, getHeight() - yMargin, 360 - incomeAngle / 2, incomeAngle, true, incomePaint);
    }
}
