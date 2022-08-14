package com.example.anna.MenuPrincipal.Routes.Verification;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.anna.R;

public class StationConfirmationDialog extends DialogFragment {

    private ScannerFragment scannerFragment;

    public StationConfirmationDialog(ScannerFragment scannerFragment){
        this.scannerFragment = scannerFragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext()).setMessage(getString(R.string.stationVerified))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FragmentManager manager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        trans.remove(scannerFragment);
                        manager.popBackStack();
                        trans.commit();
                        StationConfirmationDialog.this.dismiss();
                    }
                }).create();
    }

}
