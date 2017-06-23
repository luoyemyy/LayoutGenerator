# LayoutGenerator

### 自动生成代码

1. 将光标放在Activity或Fragment的类中的布局文件id上
![](https://github.com/luoyemyy/LayoutGenerator/raw/master/QQ截图20170609140747.png)

2. 按alt+insert，选择LayoutGenerator
![](https://github.com/luoyemyy/LayoutGenerator/raw/master/QQ截图20170609140801.png)

3. 选择需要生成的id,findId 列生成findViewById(xxx),click 列 生成setOnClickListener(this) 和 onClick 方法中的 if 片段
![](https://github.com/luoyemyy/LayoutGenerator/raw/master/QQ截图20170609140817.png)

4. 生成代码如下

```
    public View initViewAndData(View v) {
        mTxtPhone = (TextView) v.findViewById(R.id.txtPhone);
        mTxtEmail = (TextView) v.findViewById(R.id.txtEmail);
        mTxtToRegister = (TextView) v.findViewById(R.id.txtToRegister);
        mLayout1 = (RelativeLayout) v.findViewById(R.id.layout1);
        mTxtAreaCode = (TextView) v.findViewById(R.id.txtAreaCode);
        mLayout2 = (RelativeLayout) v.findViewById(R.id.layout2);
        mLayout3 = (RelativeLayout) v.findViewById(R.id.layout3);
        mTxtPhone.setOnClickListener(this);
        mTxtEmail.setOnClickListener(this);
        mTxtToRegister.setOnClickListener(this);
        mLayout1.setOnClickListener(this);
        mTxtAreaCode.setOnClickListener(this);
        mLayout2.setOnClickListener(this);
        mLayout3.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtPhone) {
        } else if (v.getId() == R.id.txtEmail) {
        } else if (v.getId() == R.id.txtToRegister) {
        } else if (v.getId() == R.id.layout1) {
        } else if (v.getId() == R.id.txtAreaCode) {
        } else if (v.getId() == R.id.layout2) {
        } else if (v.getId() == R.id.layout3) {
        }
    }

```
