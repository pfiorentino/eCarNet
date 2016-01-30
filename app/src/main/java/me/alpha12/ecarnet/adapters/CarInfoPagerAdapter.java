package me.alpha12.ecarnet.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.classes.Utils;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.CarModel;

public class CarInfoPagerAdapter extends PagerAdapter {
    private Context mContext;
    private Car currentCar;

    public CarInfoPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
        collection.addView(layout);

        if (position == 0){
            if (currentCar.getPlateNum() != null)
                ((TextView) layout.findViewById(R.id.plate_number_text_view)).setText(currentCar.getPlateNum());
            if (currentCar.getKilometers() > 0)
                ((TextView) layout.findViewById(R.id.total_distance_text_view)).setText(currentCar.getKilometers()+" km");
            if (currentCar.getCirculationDate() != null)
                ((TextView) layout.findViewById(R.id.circulation_date_text_view)).setText(GlobalContext.getFormattedMediumDate(currentCar.getCirculationDate(), true));
            if (currentCar.getBuyingDate() != null)
                ((TextView) layout.findViewById(R.id.buying_date_text_view)).setText(GlobalContext.getFormattedMediumDate(currentCar.getBuyingDate(), true));
        } else if (position == 1) {
            if (currentCar.getCarModel() != null) {
                CarModel model = currentCar.getCarModel();
                ((TextView) layout.findViewById(R.id.brand_text_view)).setText(Utils.ucWords(model.getBrand()));
                ((TextView) layout.findViewById(R.id.model_text_view)).setText(Utils.ucWords(model.getModel()));
                ((TextView) layout.findViewById(R.id.version_text_view)).setText(Utils.ucWords(model.getVersion()+" - "+model.getEnergy()));
                ((TextView) layout.findViewById(R.id.body_text_view)).setText(Utils.ucWords(model.getBody()));
                ((TextView) layout.findViewById(R.id.gearbox_text_view)).setText(Utils.ucWords(model.getGearboxType()+" "+model.getGears()+" "+mContext.getString(R.string.gears)));
                ((TextView) layout.findViewById(R.id.rated_hp_text_view)).setText(model.getRatedHP()+" CV");
                ((TextView) layout.findViewById(R.id.mine_type_text_view)).setText(model.getMineType());
            }
        }

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return CustomPagerEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }

    public void setData(Car currentCar) {
        this.currentCar = currentCar;
    }

    public enum CustomPagerEnum {

        FIRST(R.string.pager_first_tab_title, R.layout.tab_car_info),
        SECOND(R.string.pager_second_tab_title, R.layout.tab_model_info);

        private int mTitleResId;
        private int mLayoutResId;

        CustomPagerEnum(int titleResId, int layoutResId) {
            mTitleResId = titleResId;
            mLayoutResId = layoutResId;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public int getLayoutResId() {
            return mLayoutResId;
        }

    }

}