# RecyclerViewCursorAdapter


## Usage:

```java

    void setupRecyclerView(RecyclerView recyclerView) {

        Cursor cursor = db.query("table",null,null,null,null,null,null);
        FooAdapter adapter = new FooAdapter(Foo.class, cursor);
        recyclerView.setAdapter(adapter);


    }


    class FooAdapter extends RecyclerViewCursorAdapter<Foo, FooAdapter.ViewHolder> {


        class ViewHolder extends RecyclerViewCursorAdapter.ViewHolder{
            public TextView textView;
            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.textView);
            }
        }

        public FooAdapter(@NonNull Class<Foo> klass, @Nullable Cursor cursor) {
            super(klass, cursor);
        }


        @Override
        Foo fromCursorRow(Cursor cursor) {

            Foo foo = new Foo();

            foo.title = cursor.getString(cursor.getColumnIndex("title"));

            return foo;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.listitem_foo, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.textView.setText(getItem(position).title);
        }


    }



    class Foo {
        public String title;
    }
    
```