package cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.andexert.library.RippleView;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.R;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.library.ColorGenerator;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.library.TextDrawable;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.listener.Listener;
import cupcakehakathon.com.uet.cupcake.hackathon.schedulepatient.common.object.FacultyObject;
import java.util.ArrayList;

/**
 * Created by Luong Tran on 3/11/2017.
 */

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.ViewHolder> {

    private ArrayList<FacultyObject> lsFaculty;
    private Context context;
    private Listener.listenFaculty listenFaculty;

    public class ViewHolder
        extends RecyclerView.ViewHolder {

        private RippleView rvFaculty;
        private ImageView imgLetter;
        private TextView txtName, txtDesc;

        public ViewHolder(View v) {
            super(v);
            rvFaculty = (RippleView) v.findViewById(R.id.rvFaculty);
            imgLetter = (ImageView) v.findViewById(R.id.imgLetterFaculty);
            txtName = (TextView) v.findViewById(R.id.txtFaculty);
            txtDesc = (TextView) v.findViewById(R.id.txtDescFaculty);
            txtName.setSelected(true);
        }
    }

    public FacultyAdapter(ArrayList<FacultyObject> ls, Context context) {
        this.lsFaculty = ls;
        this.context = context;
    }

    @Override
    public FacultyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faculty, parent, false);
        FacultyAdapter.ViewHolder viewHolder = new FacultyAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FacultyAdapter.ViewHolder holder, final int position) {
        FacultyObject facultyObject = lsFaculty.get(position);
        holder.txtName.setText(facultyObject.getName());
        holder.txtDesc.setText(facultyObject.getDescription());
        // get letter to image
        String firstLetter = String.valueOf(facultyObject.getName().charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL_COLOR;
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color);
        holder.imgLetter.setImageDrawable(drawable);
        holder.rvFaculty.setRippleDuration(250);
        holder.rvFaculty.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                listenFaculty.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lsFaculty.size();
    }

    public void setListenFaculty(Listener.listenFaculty listenFaculty) {
        this.listenFaculty = listenFaculty;
    }
}