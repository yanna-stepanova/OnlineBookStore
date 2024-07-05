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

<pre><code class="java">  
</code></pre><br>
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
