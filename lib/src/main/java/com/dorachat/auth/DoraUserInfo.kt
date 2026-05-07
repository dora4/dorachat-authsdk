package com.dorachat.auth

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import dora.db.constraint.Id
import dora.db.migration.OrmMigration
import dora.db.table.Column
import dora.db.table.OrmTable
import dora.db.table.Table
import java.io.Serializable

@Keep
@Table("dora_user_info")
data class DoraUserInfo(
    @SerializedName("id")
    @Id
    var id: Long = OrmTable.ID_UNASSIGNED,
    @SerializedName("erc20")
    @Column("erc20")
    var erc20: String,
    @SerializedName("nickname")
    @Column("nickname")
    var nickname: String? = null,
    @SerializedName("gender")
    @Column("gender")
    var gender: Int = 0,
    @SerializedName("introduce")
    @Column("introduce")
    var introduce: String? = null,
    @SerializedName("isUpgradeRecreated")
    override val isUpgradeRecreated: Boolean = false,
    @SerializedName("migrations")
    override val migrations: Array<OrmMigration>? = arrayOf()
) : OrmTable, Serializable {

    fun mergeRemote(remote: DoraUserInfo?, erc20: String): DoraUserInfo {
        if (remote == null) return this
        return this.apply {
            nickname = remote.nickname
                ?.takeIf { it.isNotBlank() }
                ?: substringErc20(erc20)
            gender = remote.gender.takeIf { it != 0 } ?: getDefaultGender()
            introduce = remote.introduce
                ?.takeIf { it.isNotBlank() }
                ?: getDefaultIntroduce()
        }
    }

    companion object {

        private val ERC20_REGEX = Regex("^0x[a-fA-F0-9]{40}$")

        fun isValidErc20(address: String?): Boolean {
            if (address.isNullOrBlank()) return false
            return ERC20_REGEX.matches(address)
        }

        fun substringErc20(erc20: String?): String {
            if (erc20.isNullOrBlank()) return ""
            return if (isValidErc20(erc20)) {
                erc20.substring(erc20.length - 4)
            } else {
                erc20
            }
        }

        fun getDefaultGender() : Int {
            return 0
        }

        fun getDefaultIntroduce() : String {
            return ""
        }

        fun createDefault(erc20: String): DoraUserInfo {
            return DoraUserInfo(erc20 = erc20, nickname = substringErc20(erc20),
                gender = getDefaultGender(), introduce = getDefaultIntroduce())
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as DoraUserInfo
        if (id != other.id) return false
        if (gender != other.gender) return false
        if (isUpgradeRecreated != other.isUpgradeRecreated) return false
        if (erc20 != other.erc20) return false
        if (nickname != other.nickname) return false
        if (introduce != other.introduce) return false
        if (!migrations.contentEquals(other.migrations)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + gender
        result = 31 * result + isUpgradeRecreated.hashCode()
        result = 31 * result + erc20.hashCode()
        result = 31 * result + (nickname?.hashCode() ?: 0)
        result = 31 * result + (introduce?.hashCode() ?: 0)
        result = 31 * result + (migrations?.contentHashCode() ?: 0)
        return result
    }
}