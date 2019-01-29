package io.github.spair.byond.dmm.drawer

import io.github.spair.byond.ByondTypes
import io.github.spair.byond.dmm.Dmm
import io.github.spair.byond.dmm.Tile
import io.github.spair.byond.dmm.TileItem

import java.awt.image.BufferedImage

import static io.github.spair.byond.dmi.SpriteDir.*

class RenderShell {

    private final GroovyShell shell = new GroovyShell(getClass().getClassLoader())

    private final varScripts = [:]
    private final imgScripts = [:]

    RenderShell(Dmm dmm, List<File> scripts) {
        shell.setGlobal = { name, value -> shell[name] = value }
        shell.dmm = dmm

        bindTileItemExtensions()
        bindAdditionalProcs()

        scripts.each { scriptFile ->
            def cleanFileName = scriptFile.name.take(scriptFile.name.lastIndexOf('.'))

            cleanFileName.split('\\.').with {
                switch (it[1]) {
                    case 'var':
                        varScripts[cleanFileName] = shell.parse(scriptFile)
                        break
                    case 'img':
                        imgScripts[cleanFileName] = shell.parse(scriptFile)
                        break
                    case 'glob':
                        shell.evaluate(scriptFile)
                        break
                    default:
                        throw new IllegalArgumentException("script without classification ($scriptFile.name)")
                }
            }
        }
    }

    void setProc(String name, Proc proc) {
        shell.name = proc
    }

    void executeVarScripts(TileItem item) {
        shell.src = item
        varScripts.keySet().each { key ->
            varScripts[key].run()
        }
    }

    BufferedImage executeImgScripts(TileItem item, BufferedImage sprite) {
        shell.src = item
        shell.sprite = sprite
        def finalSprite = sprite
        imgScripts.keySet().each { key ->
            finalSprite = imgScripts[key].run() as BufferedImage
        }
        return finalSprite
    }

    private void bindTileItemExtensions() {
        TileItem.metaClass.getProperty = { String propName ->
            def meta = TileItem.metaClass.getMetaProperty(propName)
            if (meta) {
                return meta.getProperty(delegate)
            } else {
                String var = delegate.getVar(propName)
                if (var.startsWith('"') && var.endsWith('"') || var.startsWith("'") && var.endsWith("'")) {
                    return var.substring(1, var.length() - 1)
                } else if (var == ByondTypes.NULL) {
                    return null
                } else if (var.isLong()) {
                    return Long.parseLong(var)
                } else if (var.isDouble()) {
                    return Double.parseDouble(var)
                } else {
                    return var
                }
            }
        }

        TileItem.metaClass.setProperty = { String propName, Object value ->
            def meta = TileItem.metaClass.getMetaProperty(propName)
            if (meta) {
                return meta.setProperty(delegate, value)
            } else {
                if (value instanceof CharSequence) {
                    if (value.startsWith("'") && value.endsWith("'")) {
                        delegate.setVar(propName, value)
                    } else {
                        delegate.setVarText(propName, value)
                    }
                } else {
                    delegate.setVar(propName, value)
                }
            }
        }

        List.metaClass.<TileItem> eachForTypeOnly { String type, Closure closure ->
            delegate.each { TileItem item ->
                if (item.isType(type)) {
                    closure(item)
                }
            }
        }
    }

    private void bindAdditionalProcs() {
        shell.oRange = { TileItem item, int range ->
            Dmm dmm = shell.dmm
            def list = [] as List

            list.addAll(dmm.getTile(item.x + range, item.y + range).tileItems)
            list.addAll(dmm.getTile(item.x + range, item.y - range).tileItems)
            list.addAll(dmm.getTile(item.x - range, item.y + range).tileItems)
            list.addAll(dmm.getTile(item.x - range, item.y - range).tileItems)

            list.addAll(dmm.getTile(item.x + range, item.y).tileItems)
            list.addAll(dmm.getTile(item.x - range, item.y).tileItems)
            list.addAll(dmm.getTile(item.x, item.y + range).tileItems)
            list.addAll(dmm.getTile(item.x, item.y - range).tileItems)

            return list
        }

        shell.getDir = { TileItem loc1, TileItem loc2 ->
            int x1 = loc1.x, x2 = loc2.x, y1 = loc1.y, y2 = loc2.y

            if (loc1.x == loc2.x && loc1.y == loc2.y)
                return 0

            def dir
            if (x1 == x2) {
                if (y1 < y2) {
                    dir = NORTH
                } else {
                    dir = SOUTH
                }
            } else if (y1 == y2) {
                if (x1 < x2) {
                    dir = EAST
                } else {
                    dir = WEST
                }
            } else if (x1 < x2 && y1 < y2) {
                dir = NORTHEAST
            } else if (x1 < x2 && y1 > y2) {
                dir = SOUTHEAST
            } else if (x1 > x2 && y1 > y2) {
                dir = SOUTHWEST
            } else {
                dir = NORTHWEST
            }
            return dir.dirValue
        }

        shell.getStep = { TileItem item, int dir ->
            def x = item.x, y = item.y
            switch (valueOfByondDir(dir)) {
                case NORTH: y++; break
                case SOUTH: y--; break
                case WEST: x--; break
                case EAST: x++; break
                case NORTHWEST: x--; y++; break
                case NORTHEAST: x++; y++; break
                case SOUTHWEST: x--; y--; break
                case SOUTHEAST: x++; y--; break
            }
            return shell.dmm.getTile(x, y)
        }

        shell.locate = { String itemType, Tile tile ->
            for (item in tile.tileItems) {
                if (item.isType(itemType)) {
                    return item
                }
            }
            return null
        }
    }
}
