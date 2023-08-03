package com.example.jujojazbase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import json.JSONObject;

public class AdapterEditRecycler extends RecyclerView.Adapter<AdapterEditRecycler.viewHolder> {
    public static List<ModelData> datas = new ArrayList<>();
    private Context context;
    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView name, detail;
        ImageView foto;
        ImageView foto_besar;
        ImageButton btnDown, btnDelete, btnEdit;
        RelativeLayout relativeLayoutEdit;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameEdit);
            detail = itemView.findViewById(R.id.detailEdit);
            foto = itemView.findViewById(R.id.fotoEdit);
            foto_besar = itemView.findViewById(R.id.img_big);
            btnDown = itemView.findViewById(R.id.btnDown);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            relativeLayoutEdit = itemView.findViewById(R.id.relativeLayoutEdit);
        }
    }

    public AdapterEditRecycler(Context context, List<ModelData> myData) {
        this.context = context;
        datas = myData;
    }

    @NonNull
    @Override
    public AdapterEditRecycler.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        parent.removeAllViews();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_edit , parent, false);
        AdapterEditRecycler.viewHolder vh = new AdapterEditRecycler.viewHolder(v);
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final AdapterEditRecycler.viewHolder holder, final int position) {
        final ModelData dataset = datas.get(position);
        //Glide.with(context)
        //        .load(dataset.get(0))
        //        .apply(RequestOptions.circleCropTransform())
        //        .into(holder.foto);
        holder.foto.setImageBitmap(stringToBitmap(dataset.getImage()));
        holder.foto_besar.setImageBitmap(stringToBitmap(dataset.getImage()));
        holder.name.setText(dataset.getCar_name());
        holder.detail.setText(dataset.getMerk());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(context, EditInformation.class);
                edit.putExtra("POSITION", position);
                edit.putExtra("ID", dataset.getId());
                context.startActivity(edit);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JujojazLib net = new JujojazLib(){
                    @Override
                    public  void onDone(List<Byte> x) {
                        Byte[] byteArray = new Byte[x.size()];
                        x.toArray(byteArray);
                        JSONObject data = new JSONObject(new String(Lib.Companion.Bytetobyte(byteArray)));
                        System.out.println(data.toString());
                        if (data.get("success").equals("1")) {
                            Auth.datas.remove(position);
                            datas.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeRemoved(position, datas.size());

                        } else {
                            System.out.println("Fail");
                            }
                        }
                    };
                JSONObject deleteJson = new JSONObject();
                deleteJson.put("username", Auth.user.getUsername());
                deleteJson.put("password", Auth.user.getPassword());
                deleteJson.put("id_kendaraan", dataset.getId());
                net.sendUrl(Configuration.Companion.getAPI_SERVER() + "/api/deletevehicle/", Lib.Companion.byteToByte(("data="+deleteJson.toString()).getBytes()), 0);
            }
        });
        holder.btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AdapterEditRecycler", "btnDown");
                dataset.setExpand(!dataset.isExpand());
                Log.d("AdapterEditRecycler", "dataset : " + dataset.isExpand());
                notifyItemChanged(holder.getAdapterPosition());
            }
        }); 
        boolean isExpand = dataset.isExpand();
        Log.d("AdapterEditRecycler", "isExpand : " + String.valueOf(isExpand));
        holder.relativeLayoutEdit.setVisibility(isExpand ? View.VISIBLE : View.GONE);
        holder.btnDown.animate().rotation(isExpand ? 180 : 0).start();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public Bitmap stringToBitmap(String encodeString) {
        try {
            byte[] encodeByte = Base64.decode(encodeString, Base64.NO_WRAP);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
