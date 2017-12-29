package com.zsh.shopclient.aView;

import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zsh.shopclient.R;
import com.zsh.shopclient.aPresenter.LaunchActivity;
import com.zsh.shopclient.aPresenter.MainActivity;

/**
 * Created by Administrator on 2017/11/7.
 */

public class LaunchV extends BaseView {
    private CountDownTimer timer;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    public void initView() {
        Glide.with(getRootView()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510061557297&di=5b3fe94ef73527adba79ed3a221f4038&imgtype=0&src=http%3A%2F%2Fwww.zhlzw.com%2FUploadFiles%2FArticle_UploadFiles%2F201204%2F20120412123914329.jpg")
                .load("http://image.tianjimedia.com/uploadImages/2014/289/01/IGS09651F94M.jpg").
                load("http://img.tupianzj.com/uploads/allimg/160719/9-160GZZ524.jpg").
                load("http://photo.l99.com/bigger/56a/1450144005696_afdkyq.jpg").
                load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510069058118&di=848ecdeb97277777593f91f11b7aa2cf&imgtype=0&src=http%3A%2F%2Fimg3.duitang.com%2Fuploads%2Fitem%2F201209%2F28%2F20120928220354_TtLZS.thumb.700_0.jpeg")
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510069086300&di=981be5ea584e41332c1ee493bcdfb683&imgtype=0&src=http%3A%2F%2Fi1.mobile-dad.com%2Fuploads%2Fjietu%2F147075785584.jpg")
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510069123528&di=63ba9591ee8a99dd2709681b1747fbe9&imgtype=0&src=http%3A%2F%2Fimg.pcgames.com.cn%2Fimages%2Fpiclib%2F201309%2F03%2Fbatch%2F1%2F192947%2F1378193724747sdcrnznjwo.jpg")
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510069149455&di=aa437746df8c80878fd4a512187de53a&imgtype=0&src=http%3A%2F%2Fi1.mobile-dad.com%2Fuploads%2Fjietu%2F147075785567.jpg")
                .load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510069253754&di=1e0c18699c4d7d28628991ac92997c3e&imgtype=0&src=http%3A%2F%2Fd.hiphotos.baidu.com%2Fimage%2Fpic%2Fitem%2F5bafa40f4bfbfbed90b1ad9472f0f736aec31f74.jpg")
                .load("http://d.hiphotos.baidu.com/image/pic/item/0bd162d9f2d3572c1a74bb188013632763d0c35b.jpg").load(R.mipmap.guide0).into((ImageView)getRootView().findViewById(R.id.image));
        final TextView time = getRootView().findViewById(R.id.text_time);
        timer = new CountDownTimer(4000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                time.setText(millisUntilFinished/1000+"s");
            }

            @Override
            public void onFinish() {
                getRootView().getContext().startActivity(new Intent(getRootView().getContext(), MainActivity.class));
                ((LaunchActivity)getRootView().getContext()).finish();
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != timer)
            timer.cancel();
    }
}
