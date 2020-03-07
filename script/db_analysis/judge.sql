SELECT education.*
FROM education
WHERE education.`school` = '东南大学' AND education.`college` NOT IN (
	'其他',
    '建筑学院',
    '机械工程学院',
    '能源与环境学院',
    '信息科学与工程学院',
    '土木工程学院',
    '电子科学与工程学院',
    '数学学院',
    '计算机科学与工程学院',
    '自动化学院',
    '物理学院',
    '生物科学与医学工程学院',
    '材料科学与工程学院',
    '人文学院',
    '经济管理学院',
    '哲学系',
    '电气工程学院',
    '外国语学院',
    '体育系',
    '化学化工学院',
    '交通学院',
    '仪器科学与工程学院',
    '旅游管理系',
    '艺术学院',
    '法学院',
    '医学院',
    '公共卫生学院',
    '吴健雄学院',
    '软件学院',
    '海外教育学院',
    '微电子学院',
    '马克思主义学院',
    '网络空间安全学院',
    '人工智能学院',
    '雷恩研究生学院',
    '蒙纳士大学苏州联合研究生院',
    '生命科学与技术学院'
)