# ExtensibleAdapter
<img src="/readme_res/screen.gif"/>
<P>How-To-Use</P><br/>
<P1>step1</P1>
if RecyclerView
create a class MyAdapter extends ExpandRecyclerAdapter<? extends ExpandRecyclerAdapter.ExpandHolder>
else 
create a class MyAdapter extends ExpandAdapter
<P1>step2</P1>
RecyclerView.setAdapter(new MyAdapter())
or
ListView.setAdapter(new MyAdapter)