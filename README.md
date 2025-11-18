# CustomServerNameAPI

## 简介
CustomServerNameAPI 专为自定义服务器名称而设计，这是一个插件化的PlaceholderAPI扩展，变量内容支持自定义
#### 理论支持全版本！

## 特性
1. **服务器名称和别名管理**：支持通过命令动态更新服务器名称和别名，满足不同场景下的展示需求。
2. **自动生成别名**：可根据服务器名称自动生成首字母大写作为别名，注意，此时设置名称为中文或其他非ASCII字符，那么此功能无效。
3. **命令帮助与补全**：提供详细的命令帮助信息，以及命令补全功能。
4. **变量持久化开关**：可以通过命令开启或关闭变量持久化功能。
5. **支持Hex颜色**：<#ffffff>你可以使用此标签来设定16进制颜色，也可以在后面跟上闭合标签</000000>来实现渐变颜色

## 命令列表
| 命令 | 描述 | 使用示例 |
| ---- | ---- | ---- |
| `/customservernameapi` 或 `/csna` | 显示插件的主要帮助信息，列出所有可用的子命令及其功能描述。 | `/customservernameapi` |
| `/customservernameapi reload` | 重新加载插件的配置文件，应用最新的配置更改。 | `/customservernameapi reload` |
| `/customservernameapi update name <新的服务器名称>` | 更新服务器的名称。如果开启了自动生成别名功能，且新名称符合条件，会自动更新别名。 | `/customservernameapi update name "My New Server"` |
| `/customservernameapi update alias <新的服务器别名>` | 更新服务器的别名。 | `/customservernameapi update alias "New Alias"` |
| `/customservernameapi update autogeneratealias` | 切换自动生成别名功能的开关状态。注意：若设置的内容为中文或其他非ASCII字符，则自动生成别名功能将失效。 | `/customservernameapi update autogeneratealias` |
| `/customservernameapi togglepersist` | 切换变量持久化功能的开关状态。 | `/customservernameapi togglepersist` |
| `/customservernameapi info` | 查询配置文件中的各个设置项状态，包括配置文件版本、服务器名称、别名、自动生成别名功能开关和持久化开关等。 | `/customservernameapi info` |
| `/customservernameapi help` | 显示与一级默认命令相同的帮助信息。 | `/customservernameapi help` |

## 权限节点
本插件无普通用户权限，所有权限统一为 `customservernameapi.admin`

## 安装与配置
1. **安装**：将插件的jar文件放置在Bukkit服务器的`plugins`文件夹中，重启服务器即可完成安装。
2. **配置**：
    - 首次启动插件时，会自动生成默认配置文件`config.yml`。
    - 配置文件中包含服务器名称、别名、自动生成别名开关、持久化开关等配置项，可以根据需求进行修改。

## 注意事项
1. 使用自动生成别名功能时，若设置的名称为中文或其他非ASCII字符，该功能将失效。
2. 在更新配置文件时，尽量避免手动修改配置文件中的版本号，以免影响插件的自动更新机制。
3. 插件的所有功能需要管理员权限才能使用，确保具有`customservernameapi.admin`权限节点。
4. 配置文件就版本号请勿修改，可能会导致插件无法使用

## 兼容性
本插件尽量保证与不同版本的 Bukkit 和 Spigot 服务器兼容，但由于不同版本的 API 可能存在差异，建议在使用前进行测试。

## 问题反馈与支持
如果你在使用过程中遇到任何问题或有改进建议，请提交 Issues，我们将尽快处理。

## 贡献代码
欢迎开发者为该插件贡献代码。如果你有好的想法或改进方案，请提交 Pull Request，我们会认真审核并合并优秀的贡献。

## 联系方式
如果在使用过程中遇到任何问题或有任何建议，欢迎联系插件开发者：
- **QQ**：1498640871

## 许可证
本插件遵循 MIT 许可证进行发布，具体内容请查看 LICENSE 文件。

