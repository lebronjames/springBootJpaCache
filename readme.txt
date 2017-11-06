一、Jpa的使用
1)
<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		
		<dependency>
	        <groupId>mysql</groupId>
	        <artifactId>mysql-connector-java</artifactId>
	    </dependency>
2)@Entity//说明这个class是实体类，并且使用默认的orm规则，即class名即数据库表中表名，class字段名即表中的字段名
@Table(name="tb_user")
public class User {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="user_name",nullable=false)
	private String name;
	
	@Column(name="user_age",nullable=false)
	private int age;
3)spring.datasource.url=jdbc:mysql://10.5.2.242:3306/migu?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.jpa.properties.hibernate.hbm2ddl.auto=update
spring.jpa.properties.hibernate.show_sql=true
4)
@RestController
@RequestMapping("/user")
@Api(value = "user", description = "用户管理")
public class UserController {

private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/save")
	public void save(@RequestParam(value="name")String name,@RequestParam(value="age")String age) {
		userRepository.save(new User(name,Integer.valueOf(age)));
	}
	
	@GetMapping("/list")
	public List<User> list() {
		return userRepository.findAll();
	}
}


二、 SpringCache集成(EhCache/Redis)
1)在pom.xml中引入cache/ehcache(net.sf.ehcache)依赖
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
2)在Spring Boot主类中增加@EnableCaching注解开启缓存功能
3)在数据访问接口中，增加缓存配置注解
@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User, Long> {
    //key：缓存对象存储在Map集合中的key值.#p0使用函数第一个参数作为缓存的key值
	//condition：缓存对象的条件，非必需，只有满足表达式条件的内容才会被缓存
	@Cacheable(key="#p0",condition="#p0.length()<10")//配置了findUser函数的返回值将被加入缓存
	@Query("from User u where u.name=:name")
	User findUser(@Param("name") String name);
}
4)在src/main/resources目录下创建：ehcache.xml
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="ehcache.xsd">
    <cache name="users"
           maxEntriesLocalHeap="200"
           timeToLiveSeconds="600">
    </cache>
</ehcache>
5)Redis配置
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.pool.max-idle=8
spring.redis.pool.min-idle=0
spring.redis.pool.max-active=8
spring.redis.pool.max-wait=-1
6)
@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable(key = "#p0")
    User findByName(String name);

    @CachePut(key = "#p0.name")
    User save(User user);

}
7)@CachePut：配置于函数上，能够根据参数定义条件来进行缓存，它与@Cacheable不同的是，它每次都会真是调用函数，所以主要用于数据新增和修改操作上。它的参数与@Cacheable类似，具体功能可参考上面对@Cacheable参数的解析
@CacheEvict：配置于函数上，通常用在删除方法上，用来从缓存中移除相应数据。除了同@Cacheable一样的参数之外，它还有下面两个参数：
allEntries：非必需，默认为false。当为true时，会移除所有数据
beforeInvocation：非必需，默认为false，会在调用方法之后移除数据。当为true时，会在调用方法之前移除数据。




