# ExtensibleAdapter
<img src="/readme_res/screen.gif"/>
<P>How-To-Use</P><br/>
<P1>step1</P1><br/>
if RecyclerView<br/>
create a class MyAdapter extends ExpandRecyclerAdapter<? extends ExpandRecyclerAdapter.ExpandHolder><br/>
else <br/>
create a class MyAdapter extends ExpandAdapter<br/>
<P1>step2</P1><br/>
RecyclerView.setAdapter(new MyAdapter())<br/>
or<br/>
ListView.setAdapter(new MyAdapter)

``` xml

dependencies {
    compile 'com.cabe.lib.ui:ExtensibleAdapter:1.0.0'
}

```