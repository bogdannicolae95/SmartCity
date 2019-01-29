package com.example.nicolaebogdan.smartcity.i.abstr;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolaebogdan.smartcity.i.FragmentView;
import com.example.nicolaebogdan.smartcity.i.MainView;

import androidx.navigation.Navigation;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class AbstractFragment<MV extends MainView , FP extends AbstractFragmentPresenter> extends Fragment implements FragmentView {

    protected FP fragmentPresenter;
    private Unbinder unbinder;
    protected abstract @LayoutRes int getLayoutResId();

    @Override
    public FP createFragmentPresenter(){ return fragmentPresenter;};

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentPresenter = createFragmentPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public MV getActivityView(){
        return (MV) this.getActivity();
    }

    @Override
    public void goBack(){
        Navigation.findNavController(getView()).navigateUp();
    }

    public void openDrawer() {
        getActivityView().toggleNavigationDrawer();
    }
    public void navigateTo(@IdRes int destination){Navigation.findNavController(getView()).navigate(destination);}
    public void navigateTo(@IdRes int destination, Bundle args){Navigation.findNavController(getView()).navigate(destination, args);}
}
