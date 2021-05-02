package com.project.cointerest

import androidx.recyclerview.widget.DiffUtil

class DiffCallback : DiffUtil.ItemCallback<CoinInfo>() {
    override fun areItemsTheSame(oldItem: CoinInfo, newItem: CoinInfo) =
        oldItem.price == newItem.price

    override fun areContentsTheSame(oldItem: CoinInfo, newItem: CoinInfo) =
        oldItem == newItem
}