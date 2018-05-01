package com.jamargle.bakineando.presentation.recipedetail.adapter;

public abstract class RecipeItem<T> {

    public static final int INTRODUCTION = 1;
    public static final int INGREDIENT = 2;
    public static final int STEP = 3;

    protected int viewType;
    protected T item;

    public int getViewType() {
        return viewType;
    }

    public T getItem() {
        return item;
    }

}
