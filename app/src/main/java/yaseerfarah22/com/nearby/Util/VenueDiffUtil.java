package yaseerfarah22.com.nearby.Util;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import es.dmoral.toasty.Toasty;
import yaseerfarah22.com.nearby.Model.POJO.Venue;
import yaseerfarah22.com.nearby.View.MainActivity;


/**
 * Created by DELL on 9/13/2019.
 */

public class VenueDiffUtil extends DiffUtil.Callback {

    private List<Venue> oldList;
    private List<Venue> newList;

    Context context;

    public VenueDiffUtil(Context context, List<Venue> oldList, List<Venue> newList) {
        this.oldList = oldList;
        this.newList = newList;
        this.context=context;
    }



    @Override
    public int getOldListSize() {


        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId().trim().matches(newList.get(newItemPosition).getId().trim());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).compare(newList.get(newItemPosition));
    }


    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        Venue newModel = newList.get(newItemPosition);
        Venue oldModel = oldList.get(oldItemPosition);

        Bundle diff = new Bundle();


            diff.putString("title", newModel.getName());

            diff.putString("address",newModel.getLocation().getAddress());



        if (diff.size() == 0) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
        return diff;

        //return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
