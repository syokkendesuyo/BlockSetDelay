package net.jp.minecraft.plugin;

import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcstats.Metrics;

/**
 * MinecraftでAnnihilationというゲームを提供しているサーバのように
 * 一定時間経過後再度ブロックが現れる機能を追加するプラグイン
 * Annihilationとは同様の挙動ではないので注意が必要
 * BlockSetDelay
 * @author syokkendesuyo
 */


public class BlockSetDelay extends JavaPlugin implements Listener {


	/**
	 * プラグインが有効になったときに呼び出されるメソッド
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);

		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			// ｽﾃｲﾀｽの送信に失敗 :-(
		}

    }


	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {

		//プレイヤー変数
		Player player = event.getPlayer();

		//破壊場所のワールド検索変数
		World world = event.getBlock().getWorld();

		//破壊場所の特定
		int x = event.getBlock().getX();
		int y = event.getBlock().getY();
		int z = event.getBlock().getZ();

		//場所の値をblock変数に代入
		final Block block = world.getBlockAt(x, y, z);

		//破壊されたブロックデータ保管するtype変数
		final Material type = event.getBlock().getType();

		//破壊されたブロックのメタデータを保管するmeta変数
		final byte meta = event.getBlock().getData();

		//破壊までの時間を決める変数
		int time = 20*60;

		//もしOPだった場合、または"bsd.allow"パーミッションを持っていた場合はブロックを破壊しても再設置しない
		if(player.isOp()||player.hasPermission("bsd.allow")){
			//何もしない
		}
		//OPも"bsd.allow"も持っていなかった場合再設置する
		else{
			//破壊場所の座標を取得
			new BukkitRunnable() {
				@Override
				public void run() {
					//ブロックを再設置
					block.setType(type);
					//メタを設定
					block.setData(meta);
				}
			}.runTaskLater(this, time);
		}
	}
}