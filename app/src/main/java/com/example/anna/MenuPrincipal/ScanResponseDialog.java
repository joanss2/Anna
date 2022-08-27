package com.example.anna.MenuPrincipal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.anna.MenuPrincipal.Discounts.Verification.ScannerFragmentDiscount;
import com.example.anna.MenuPrincipal.Routes.Verification.ScannerFragmentStation;
import com.example.anna.R;

public class ScanResponseDialog extends DialogFragment {

    private ScannerFragmentStation scannerFragmentStation;
    private ScannerFragmentDiscount scannerFragmentDiscount;
    private String message;

    public ScanResponseDialog(ScannerFragmentStation scannerFragmentStation, String message) {
        this.scannerFragmentStation = scannerFragmentStation;
        this.message = message;
    }

    public ScanResponseDialog(ScannerFragmentDiscount scannerFragmentDiscount, String message){
        this.scannerFragmentDiscount = scannerFragmentDiscount;
        this.message = message;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        if(message.equals(getString(R.string.stationVerified)) || message.equals(getString(R.string.firstStation)) || message.equals(getString(R.string.discountVerified))){
            return new AlertDialog.Builder(requireContext()).setMessage(message)
                    .setTitle(getString(R.string.scanVerification))
                    .setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
                        FragmentManager manager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        if(scannerFragmentDiscount==null)
                            trans.remove(scannerFragmentStation);
                        else
                            trans.remove(scannerFragmentDiscount);
                        manager.popBackStack();
                        trans.commit();
                        ScanResponseDialog.this.dismiss();
                    }).setIcon(R.drawable.ic_tick).create();
        }else if(message.equals(getString(R.string.stationAlreadyVisited)) || message.equals(getString(R.string.stationRejected)) || message.equals(getString(R.string.discountRejected))
                || message.equals(getString(R.string.discountAlreadyUsed))){
            return new AlertDialog.Builder(requireContext()).setMessage(message)
                    .setTitle(getString(R.string.scanVerification))
                    .setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
                        FragmentManager manager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        if(scannerFragmentDiscount==null)
                            trans.remove(scannerFragmentStation);
                        else
                            trans.remove(scannerFragmentDiscount);
                        manager.popBackStack();
                        trans.commit();
                        ScanResponseDialog.this.dismiss();
                    }).setIcon(R.drawable.ic_cross).create();
        }else{
            return new AlertDialog.Builder(requireContext()).setMessage(message)
                    .setTitle(getString(R.string.scanVerification))
                    .setPositiveButton(getString(R.string.ok), (dialogInterface, i) -> {
                        FragmentManager manager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();
                        if(scannerFragmentDiscount==null)
                            trans.remove(scannerFragmentStation);
                        else
                            trans.remove(scannerFragmentDiscount);
                        manager.popBackStack();
                        trans.commit();
                        ScanResponseDialog.this.dismiss();
                    }).setIcon(R.drawable.ic_cup).create();
        }

    }

}
