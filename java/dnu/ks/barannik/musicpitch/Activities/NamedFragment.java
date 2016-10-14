package dnu.ks.barannik.musicpitch.Activities;

import android.support.v4.app.Fragment;

public abstract class NamedFragment extends Fragment {

    public NamedFragment() {
        super();
        name = "No name";
    }

    public abstract void onSelect();
    public abstract void onDeselect();

    protected String name;

}
