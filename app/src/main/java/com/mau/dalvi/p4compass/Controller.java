package com.mau.dalvi.p4compass;

public class Controller {

    private MainActivity mainActivity;
    private CompassFragment compassFragment;
    private LoginFragment loginFragment;

    public Controller(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        compassFragment = new CompassFragment();
        loginFragment = new LoginFragment();

        mainActivity.setFragment(loginFragment, false);
        loginFragment.setController(this);
    }

        public void btnLoginClicked(){

        }


}
