package co.project.cosy;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

class ImageAdepter2 extends PagerAdapter{
    Context Context;

    ImageAdepter2(cardetaling context, Integer[] IMAGEArray) {
        this.Context = context;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    private int[] sliderImageIda = new int[]{
            R.drawable.car_wash,R.drawable.car_wash, R.drawable.car_wash,
    };

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageViewe = new ImageView(Context);
        imageViewe.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageViewe.setImageResource(sliderImageIda[position]);
        ((ViewPager) container).addView(imageViewe, 0);
        return imageViewe;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return sliderImageIda.length;
    }
}
