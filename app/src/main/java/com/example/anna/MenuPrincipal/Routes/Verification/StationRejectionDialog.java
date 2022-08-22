package com.example.anna.MenuPrincipal.Routes.Verification;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.anna.R;

public class StationRejectionDialog extends DialogFragment {
    private final ScannerFragment scannerFragment;

    public StationRejectionDialog(ScannerFragment scannerFragment){
        this.scannerFragment = scannerFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext()).setMessage(getString(R.string.stationRejected))
                .setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
                    FragmentManager manager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction trans = manager.beginTransaction();
                    trans.remove(scannerFragment);
                    manager.popBackStack();
                    trans.commit();
                    StationRejectionDialog.this.dismiss();
                }).create();
    }

}
