# RecyclerViewCursorAdapter


A CursorAdapter for the RecyclerView



## Example:

### Listing fruits in a fruitbasket from SQLite using CursorAdapter and RecyclerView


Create a class that represents the Fruit.


```
    class Fruit {
        public String title;
        public String description;
    }
    
```





Implement the RecyclerviewCursorAdapter and the RecyclerViewCursorAdapter.ViewHolder as per the ViewHolder pattern


```
    class FruitBasketAdapter extends RecyclerViewCursorAdapter<Fruit, FruitBasketAdapter.ViewHolder> {



        class ViewHolder extends RecyclerViewCursorAdapter.ViewHolder{
        
            public TextView titleTextView;
            public TextView descriptionTextView;
            
            public ViewHolder(View itemView) {
                super(itemView);
                titleTextView = (TextView) itemView.findViewById(R.id.textView1);
                descriptionTextView = (TextView) itemView.findViewById(R.id.textView2);
            }
        }




        public FruitBasketAdapter(@NonNull Class<Fruit> klass, @Nullable Cursor cursor) {
            super(klass, cursor);
        }


        @Override
        Fruit fromCursorRow(Cursor cursor) {

            Fruit Fruit = new Fruit();

            Fruit.title = cursor.getString(cursor.getColumnIndex("title"));
            Fruit.description = cursor.getString(cursor.getColumnIndex("description"));

            return Fruit;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.listitem_Fruit, parent, false));
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
        
            holder.titleTextView.setText(getItem(position).title);
            holder.descriptionTextView.setText(getItem(position).description);
            
        }


    }

``` 




Load data from SQLite or elsewhere, instantiate the adapter and attach it to the recyclerview.

```java

    void setupRecyclerView(RecyclerView recyclerView) {

        Cursor cursor = db.query("table", null, null, null, null, null, null);
        FruitBasketAdapter adapter = new FruitBasketAdapter(Fruit.class, cursor);
        recyclerView.setAdapter(adapter);


    }

```

