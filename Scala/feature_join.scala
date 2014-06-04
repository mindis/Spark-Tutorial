package cn.edu.xmu.datamining.tangzk.alibigdata

import scala.collection.{immutable, mutable}
import scala.io.Source
import java.io.{BufferedWriter, FileWriter}
import org.apache.commons.cli.{HelpFormatter, BasicParser, Options}

/**
 * 拼接features工具
 */
object FeatureJoin {

	// userId -> userFeatures
	val userFeatures = new mutable.HashMap[String, mutable.ArrayBuffer[Double]]
		// brandId -> brandFeatures
		val brandFeatures = new mutable.HashMap[String, mutable.ArrayBuffer[Double]]
		// userId-brandId -> combinedFeatures
		val combinedFeatures = new mutable.HashMap[String, mutable.ArrayBuffer[Double]]

		def main(args: Array[String]) {
			val parser = new BasicParser
				val formatter = new HelpFormatter
				val options = new Options
				val fileOption = new org.apache.commons.cli.Option("f", "files", true, "list of files")
				fileOption.setRequired(true)
				options.addOption(fileOption)
				options.addOption("t", "types", true, "list of types, 1 for usres, 2 for brands, 3 for user-brand.")
				options.addOption("c", "cols", true, "list of columns, for which column you need, example: 1,3-5, means (1,3,4,5)")
				options.addOption("id", "idfile", true, "id file, each line is an user-brand pair")
				options.addOption("ot", "outtype", true, "output type, 1 for csv, 2 for libfm")
				options.addOption("o", "out", true, "output file path")
				val cmd = parser.parse(options, args)
				//    if (args.length < 4) {
				//      System.err.println("Usage: prog <filelist> <typelist> <columnlist> <idFile> <outputType> <outputFile>")
				//      System.exit(-1)
				//    }
				//    val (filelist, typelist, columnlist, idFile, outputType, outputFile) = (args(0), args(1), args(2), args(3), args(4), args(5))
				val filelist = cmd.getOptionValue("f")
				val typelist = cmd.getOptionValue("t")
				val columnlist = cmd.getOptionValue("c")
				val idFile = cmd.getOptionValue("id")
				var outputType = cmd.getOptionValue("ot")
				if (outputType == null) {
					outputType = "1"
				}
			val outputFile = cmd.getOptionValue("o")
				if (args.length < 4) {
					formatter.printHelp("FeatureJoin", options)
						System.exit(-1)
				}
			val files = filelist.split("|")
				val types = typelist.split("|")
				val columnStrs = columnlist.split("|")
				val columns = columnStrs.map(parseColumns)
				for (i <- 0 until files.size) {
					val (f, t, c) = (files(i), types(i), columns(i))
						val data = Source.fromFile(f).getLines.flatMap {
							line => val flds = line.split("\t")
								if (flds.length < 2) {
									System.err.println(s"error format in ${f}:${t}(${line}).")
										Nil
								} else {
									val features = flds(1).split(",| ").map(_.toDouble)
										Some((flds(0), features))
								}
						}.toMap
					// 过滤出所需要的列
					val validData: immutable.Map[String, Array[Double]] = data.map {
						case (id, x) =>
							val filteredFeas = x.zipWithIndex.collect {
								// TODO 不生成columns，而是直接使用partial function来判断
								case (e, i) if (c.contains(i)) => e
							}
						(id, filteredFeas)
					}
					val currentData = t.toInt match {
						case 1 => userFeatures // 为用户特征
							case 2 => brandFeatures // 为brand特征
							case 3 => combinedFeatures // 为组合特征
					}
					if (validData.size != currentData.size) {
						// 数据集长度不一致，类型错误
						System.err.println(s"error data size in ${f}:${t}, ${validData.size} != ${currentData.size}, and skip this file.")
					} else {
						for ((key, value) <- validData) {
							currentData(key) ++= value
						}
					}
				}
			outputType.toInt match {
				case 1 =>
					writeCsvOut(idFile, outputFile)
					case 2 =>
					writeLibFmOut(idFile, outputFile)
			}
		}

	def writeCsvOut(idFile: String, outputFile: String) {
		val bwOut = new BufferedWriter(new FileWriter(outputFile))
			// 针对id文件进行操作，格式：classLabel \t userId \t brandId
			val idData = Source.fromFile(idFile).getLines().map(_.split("\t| |,"))
			for (line <- idData) {
				val (classLabel, userId, brandId) = (line(0), line(1), line(2))
					bwOut.write(s"${classLabel}")
					bwOut.write(s" ${userFeatures(userId).mkString(" ")}") // user features
					bwOut.write(s" ${brandFeatures(brandId).mkString(" ")}") // brand features
					val comFeas = combinedFeatures(s"${userId}-${brandId}")
					if (comFeas != null && comFeas.size > 0) {
						bwOut.write(s" ${comFeas.mkString(" ")}") // combined features
					}
				bwOut.write("\n")
			}
		bwOut.close()
	}

	def writeLibFmOut(idFile: String, outputFile: String) {

	}

	/**
	 * 从字符串返回列号
	 * @param str
	 * @return 数组序号
	 */
	def parseColumns(str: String): Seq[Int] = {
		// TODO 文字描述all
		str.split(",").flatMap {
			s =>
				if (s.indexOf("-") > 0) {
					// 连续记号
					val flds = s.split("-").map(_.toInt)
						flds(0) until flds(1)
				} else {
					// 单个列号
					Some(s.toInt)
				}
		}
	}

}
