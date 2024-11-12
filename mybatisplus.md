#mybatisPlus的应用

##插入一条记录（选择字段，策略插入）
```
boolean save(T entity);
返回值： boolean，表示插入操作是否成功。
对应的sql语句：INSERT INTO user (name, email) VALUES ('John Doe', 'john.doe@example.com')
```

##根据 ID 删除
```
boolean removeById(Serializable id);
返回值： boolean，表示删除操作是否成功。
对应的sql语句：DELETE FROM user WHERE name = 'John Doe'
```

##根据 ID 选择修改
```
boolean updateById(T entity);
返回值： boolean，表示更新操作是否成功。
对应的sql语句：UPDATE user SET email = 'john.doe@newdomain.com' WHERE name = 'John Doe'
```

##根据 ID 查询
```
T getById(Serializable id);
返回值： 查询结果，可能是实体对象、Map 对象或其他类型。
对应的sql语句：SELECT * FROM user WHERE id = 1
```

##查询所有
```
List<T> list();
返回值： 查询结果，可能是实体对象、Map 对象或其他类型。
对应的sql语句：SELECT * FROM user
```