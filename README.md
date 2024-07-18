<h1 style="color: #5e9ca0;">A simple Online BookStore service.</h1>
<p>Nowadays, it's very popular to use web services. That's why this project was built using MVC web infrastructure (Spring Web MVC).<br>There are implemented services for online book shopping. Book objects are stored in a MySQL database which was configured based on Liquibase scripts. and are accessed through Spring JPA. The Spring Web model-view-controller (MVC) layer allows access over the internet using RESTful API.</p>
<h3 style="color: #2e6c80;">The technologies and tools used:</h3>
<table>
  <tbody>
    <tr>
      <td>Lombok</td>
      <td>This is a Java library that automatically plugs into your editor and builds tools, spicing up your Java. Never write another getter or equals method again, with one annotation your class has a fully featured builder, automates your logging variables, and much more...</td>
    </tr>
    <tr>
      <td>MapStruct</td>
      <td>This is a Java annotation processor for the generation of type-safe and performant mappers for Java bean classes.</td>
    </tr>
    <tr>
      <td>Spring Data JPA</td>
      <td>It is an abstraction layer that sits on top of the Java Persistence API. It is used to access the database.</td>
    </tr>
    <tr>
      <td>Spring Data REST</td>
      <td>Spring Data REST builds on top of Spring Data repositories, analyzes your application&rsquo;s domain model, and exposes hypermedia-driven HTTP resources for aggregates contained in the model.</td>
    </tr>
    <tr>
      <td>Spring MVC</td>
      <td>It is used to break up a large application into smaller sections. Each section of the project is designed to illustrate its function.</td>
    </tr>
    <tr>
      <td>Spring Security</td>
      <td>This framework targets two major areas of application authentication and authorization. Authentication is the process of knowing and identifying the user that wants to access.</td>
    </tr>
    <tr>
      <td>Spring Boot Testing</td>
      <td>It is an integral part of software development. This ensures that the program works as it should and will continue to do so even as it evolves.</td>
    </tr>
    <tr>
      <td>Liquibase support</td>
      <td>This is a tool that helps developers manage and track changes to a database's structure over time. It provides an easy way to version database changes and apply them consistently across different environments. This can help ensure that database changes are made correctly and reliably, without manual intervention.</td>
    </tr>
    <tr>
      <td>Docker</td>
      <td>It is a tool for deploying and running executables in isolated and reproducible environments. This may be useful, for example, to test code in an environment identical to production.</td>
    </tr>
    <tr>
      <td>Postman</td>
      <td>It's a popular API development tool that allows users to send and receive HTTP requests.</td>
    </tr>
    <tr>
      <td>Jackson</td>
      <td>It renders the content of the response as JSON, using the class ObjectMapper of this library.</td>
    </tr>
    <tr>
      <td>Swagger</td>
      <td>It is intended for describing, creating, and visualizing the operation of REST applications.</td>
    </tr>
  </tbody>
</table>
<p>The structure of this application is built on the <abbr title="Model View Controller">MVC</abbr>  pattern.</p>
<img src="/img/diagram_mvc.png" alt="Diagram of pattern MVC" style="width:605px;height:392px;">  
<p>There are the following components:</p>
 <ol>
  <li><i>Model</i> : it's the most independent part. The Model doesn't need to know anything about the View and Controller components. This component stores data and logic. For instance, a Controller object will retrieve customer information from a database. Data is transferred between the controller components or between business logic elements.</li>
  <li><i>View</i> : this component provides information from the Model in a user-friendly format. The View mustn't change the model, because it is created based on the data collected from the model.</li>
  <li><i>Controller</i> : it processes the user's actions. The user makes changes to the data that is stored in the model through the Controller.</li>
 </ol>
<p> In this app the models (entities) are:</p>
 <ul>
    <li><u>User</u>: Contains information about the registered user including their authentication details and personal information. </li>
    <li><u>Role</u>: Represents the role of a user in the system, for example, admin or user.</li>
    <li><u>Book</u>: Represents a book available in the store.</li>
    <li><u>Category</u>: Represents a category that a book can belong to.</li>
    <li><u>ShoppingCart</u>: Represents a user's shopping cart.</li>
    <li><u>CartItem</u>: Represents an item in a user's shopping cart.</li>
    <li><u>Order</u>: Represents an order placed by a user.</li>
    <li><u>OrderItem</u>: Represents an item in a user's order.</li>
 </ul>
<p>An example of a view is a response to a request in Postman:</p>
<pre><code class="language-json">[
    {
        "id": 2,
        "title": "Cinder",
        "author": "Marissa Meyer",
        "price": 306.00,
        "isbn": "2982541266483",
        "categoryIds": [
            2,
            3
        ],
        "description": "The Lunar Chronicles. Part 1",
        "coverImage": "http://example.com/cover_Cinder.jpg"
    },
    {
        "id": 3,
        "title": "Scarlet",
        "author": "Marissa Meyer",
        "price": 307.00,
        "isbn": "8367285222976",
        "categoryIds": [
            2
        ],
        "description": "The Lunar Chronicles. Part 2",
        "coverImage": "http://example.com/cover_Scarlet.jpg"
    }
]
</code></pre>
<p>There're 5 controllers:</p>
<ul>
    <li><u>AuthenticationController</u> is indicated possible actions for the user: registration and login.</li>
    <li><u>BookController</u> gives the user access to the available assortment of books, and the admin has more advanced access(creating, updating and deleting a book).</li>
    <li><u>CategoryController</u> helps the user organize the books and the admin can manage the categories(create/update/delete)</li>
    <li><u>ShoppingCartController</u> allows the user (not admin!) to add books to their basket and edit their number or delete them from it.</li>
    <li><u>OrderController</u> can form new order just for users from their basket, can control order's status and give an information about all user's orders or certain order.</li>    
</ul>
<p>UML diagram of models was created using the plugin "PlantUml Integration":</p>
<img src="/img/uml-model.png" alt="Diagram of pattern MVC" style="width:1000px;height:600px;">  
<p>Using Swagger UI we have the visual documentation that makes it easy for back-end implementation and client-side consumption.Type in your browser 'http://localhost:8080/api/swagger-ui/index.html', when this application is running. In the beginning, it is appeared an authentication window:</p>
<img src="/img/auth.png" alt="Authentication window of the user" style="width:398px;height:315px;">  
<p>After successful authentication you can see all endpoints of this application.</p>
<img src="/img/swagger2.png" alt="All endpoints - part 1" style="width:800px;height:650px;">
<img src="/img/swagger3.png" alt="All endpoints- part 2" style="width:800px;height:650px;">
<p>Let's receive a book by id:</p>
<img src="/img/swagger4.png" alt="Example of work" style="width:600px;height:1000px;">
<p>User with role "ROLE_USER" can't create or delete entities from DB. These operations can be executed by user with role "ROLE_ADMIN". But work with shopping cart and orders is available just for role "ROLE_USER". And this user can't see or interact with another user's entities.</p>
<p>Another way to visualize a work of this application to use Postman. There are a few examples of it:</p>
<img src="/img/postman1.png" alt="Postman: all books" style="width:700px;height:850px;">
<img src="/img/postman2.png" alt="Postman: find all books by author" style="width:700px;height:850px;">
<img src="/img/postman4.png" alt="Postman: update book's information by admin" style="width:700px;height:650px;">
<p>This is what will happen if the user tries to change the data in the book (this operation is allowed for the admin):</p>
<img src="/img/postman5.png" alt="Postman: update book's information by user(forbidden)" style="width:700px;height:500px;">
<p>The user 'admin' can't have own shopping cart: </p>
<img src="/img/postman6.png" alt="Postman: get current shopping cart for admin(forbidden)" style="width:600px;height:580px;">
<p>Here's a situation where the user 'alice' creates a new order from her shopping cart. And we can see that it appears in the list of all orders:</p>
<img src="/img/postman3.png" alt="Postman: get current shopping cart for user" style="width:500px;height:800px;">
<img src="/img/postman7.png" alt="Postman: create new user's order from current shopping cart" style="width:400px;height:800px;">
<img src="/img/postman8.png" alt="Postman: get all orders for alice" style="width:400px;height:800px;">
<p>This is a link on video where I show how my project works: <a href="url">https://www.loom.com/share/924739e31cac4f688a44af341d736af5?sid=2173f008-29ed-4f66-90ae-1b74cd4a0e6d</a></p>
