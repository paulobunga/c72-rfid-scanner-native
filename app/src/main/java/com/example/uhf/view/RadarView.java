package com.example.uhf.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.uhf.R;
import com.rscja.deviceapi.entity.RadarLocationEntity;

import java.util.List;

/**
 * 雷达扫描组件
 * 宽高始终相等且取宽高中的最小值
 */
public class RadarView extends ConstraintLayout {

    private RadarBackgroundView radarBackgroundView;
    private RadarPanelView radarPanelView;

    public final static int CLOCK_WISE = 1;
    public final static int ANTI_CLOCK_WISE = -1;

    public RadarView(Context context) {
        super(context);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.rardar_view, this, true);

        radarBackgroundView = findViewById(R.id._radarBackgroundView);
        radarPanelView = findViewById(R.id._labelPanelView);

        // 获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarView);
        int image = typedArray.getResourceId(R.styleable.RadarView_center_image, R.drawable.phone); //设置默认图片
        final float scale = context.getResources().getDisplayMetrics().density;
        int width = typedArray.getDimensionPixelSize(R.styleable.RadarView_image_width, (int) (45 * scale + 0.5f));     //默认尺寸为45dp
        int height = typedArray.getDimensionPixelSize(R.styleable.RadarView_image_height, (int) (103 * scale + 0.5f));  //默认尺寸为103dp

        ImageView imageView = findViewById(R.id._centerImage);
        imageView.setBackground(context.getResources().getDrawable(image));

        LayoutParams params = (LayoutParams) imageView.getLayoutParams();
        params.width = width;
        params.height = height;
        imageView.setLayoutParams(params);

        typedArray.recycle();
    }

    /**
     * 设置雷达扫描动画的方向
     *
     * @param direction CLOCK_WISE:顺时针; ANTI_CLOCK_WISE:逆时针
     */
    public void setDirection(@RadarBackgroundView.RADAR_DIRECTION int direction) {
        if (direction != CLOCK_WISE && direction != ANTI_CLOCK_WISE) {
            throw new IllegalArgumentException("Use @RADAR_DIRECTION constants only!");
        }
        this.radarBackgroundView.setDirection(direction);
    }

    /**
     * 设置雷达扫描动画的起始角度
     *
     * @param startAngle 起始角度
     */
    public void setStartAngle(RadarBackgroundView.StartAngle startAngle) {
        this.radarBackgroundView.setStartAngle(startAngle);
        this.radarBackgroundView.invalidate();
    }

    /**
     * 打开雷达扫描动画
     */
    public void startRadar() {
        this.radarBackgroundView.start();
    }

    /**
     * 关闭雷达扫描动画
     */
    public void stopRadar() {
        this.radarBackgroundView.stop();
    }

    /**
     * 绑定标签数据
     *
     * @param TagList 标签集合
     * @param targetTag 目标标签
     */
    public void bindingData(List<RadarLocationEntity> TagList, String targetTag) {
        this.radarPanelView.bindingData(TagList, targetTag);
//        this.invalidate();
    }

    /**
     * 清空所有标签
     */
    public void clearPanel() {
        this.radarPanelView.clearPanel();
    }

    public void setRotation(int angle) {
        this.radarBackgroundView.setRotation(angle);
        this.radarPanelView.setRotation(angle);
    }
}
