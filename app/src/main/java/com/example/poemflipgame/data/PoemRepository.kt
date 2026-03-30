package com.example.poemflipgame.data

import com.example.poemflipgame.model.Poem

object PoemRepository {
    
    private val poems = listOf(
        Poem(1, "静夜思", "李白", "唐", "床前明月光，疑是地上霜。举头望明月，低头思故乡。", listOf("床","前","明","月","光")),
        Poem(2, "春晓", "孟浩然", "唐", "春眠不觉晓，处处闻啼鸟。夜来风雨声，花落知多少。", listOf("春","眠","不","觉","晓")),
        Poem(3, "登鹳雀楼", "王之涣", "唐", "白日依山尽，黄河入海流。欲穷千里目，更上一层楼。", listOf("白","日","依","山","尽")),
        Poem(4, "江雪", "柳宗元", "唐", "千山鸟飞绝，万径人踪灭。孤舟蓑笠翁，独钓寒江雪。", listOf("千","山","鸟","飞","绝")),
        Poem(5, "相思", "王维", "唐", "红豆生南国，春来发几枝。愿君多采撷，此物最相思。", listOf("红","豆","生","南","国")),
        Poem(6, "咏鹅", "骆宾王", "唐", "鹅鹅鹅，曲项向天歌。白毛浮绿水，红掌拨清波。", listOf("鹅","鹅","鹅","曲","项")),
        Poem(7, "悯农", "李绅", "唐", "锄禾日当午，汗滴禾下土。谁知盘中餐，粒粒皆辛苦。", listOf("锄","禾","日","当","午")),
        Poem(8, "草", "白居易", "唐", "离离原上草，一岁一枯荣。野火烧不尽，春风吹又生。", listOf("离","离","原","上","草")),
        Poem(9, "绝句", "杜甫", "唐", "两个黄鹂鸣翠柳，一行白鹭上青天。窗含西岭千秋雪，门泊东吴万里船。", listOf("两","个","黄","鹂","鸣")),
        Poem(10, "望庐山瀑布", "李白", "唐", "日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。", listOf("日","照","香","炉","生"))
    )
    
    fun getRandomPoem(excludeIds: Set<Int>): Poem {
        val availablePoems = poems.filter { it.id !in excludeIds }
        return if (availablePoems.isNotEmpty()) {
            availablePoems.random()
        } else {
            poems.random() // 如果都用过了，重新开始
        }
    }
    
    fun getAllPoems(): List<Poem> = poems
    
    fun getPoemCount(): Int = poems.size
}