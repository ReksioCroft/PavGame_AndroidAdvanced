package ro.tav.pavgame.presentation.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import ro.tav.pavgame.R;

public class CameraFragment extends Fragment {
    private ImageView photo;
    private ActivityResultLauncher < Intent > cameraLauncher;

    @Nullable
    @Override
    public View onCreateView( @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ) {
        final View view = super.onCreateView( inflater, container, savedInstanceState );

        cameraLauncher = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if ( result.getResultCode() == Activity.RESULT_OK ) {
                        // There are no request codes
                        Intent data = result.getData();
                        if ( data != null ) {
                            Bitmap captureImage = ( Bitmap ) data.getExtras().get( "data" );
                            photo.setImageBitmap( captureImage );
                        }
                    }
                } );

        return view;
    }

    public CameraFragment( int contentLayoutId ) {
        // Required empty public constructor
        super( contentLayoutId );
    }

    public CameraFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated( view, savedInstanceState );

        photo = requireView().findViewById( R.id.photo );
        Button openCamera = requireView().findViewById( R.id.open_camera );

        if ( ContextCompat.checkSelfPermission( requireContext(), Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( requireActivity(), new String[] { Manifest.permission.CAMERA }, 100 );
        }

        openCamera.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
                cameraLauncher.launch( intent );
            }
        } );
    }


}
