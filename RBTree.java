import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.FileWriter;

class Node {
  int value;
  int color;
  Node parent;
  Node left;
  Node right;
  int label;
}
  
public class RBTree {
  public Node root;
  public Node Null_Node;

  public RBTree() {
    Null_Node = new Node();
    Null_Node.color = 0;
    Null_Node.left = null;
    Null_Node.right = null;
    root = Null_Node;
  }

  public Node findPredecessor(Node node) {
    if (node.left != Null_Node) {
        // If the node has a left child, the predecessor is the rightmost node in the left subtree
        Node predecessor = node.left;
        while (predecessor.right != Null_Node) {
            predecessor = predecessor.right;
        }
        return predecessor;
    } else {
        // If the node does not have a left child, the predecessor is the highest ancestor whose right child is also an ancestor of the given node
        Node current = node;
        Node parent = current.parent;
        while (parent != Null_Node && current == parent.left) {
            current = parent;
            parent = parent.parent;
        }
        return parent;
    }
  }

  public Node findSuccessor(Node node) {
    if (node.right != Null_Node) {
        // If the node has a right child, the successor is the leftmost node in the right subtree
        Node successor = node.right;
        while (successor.left != Null_Node) {
            successor = successor.left;
        }
        return successor;
    } 
    else {
        // If the node does not have a right child, the successor is the lowest ancestor whose left child is also an ancestor of the given node
        Node current = node;
        Node parent = current.parent;
        while (parent != Null_Node && current == parent.right) {
            current = parent;
            parent = parent.parent;
        }
        return parent;
    }
  }

  public void left_rotation(Node x) {
    Node y = x.right;
    x.right = y.left;
    if (y.left != Null_Node) {
      y.left.parent = x;
    }
    y.parent = x.parent;

    if (x.parent == null) {
      this.root = y;
    } 
    else if (x == x.parent.left) {
      x.parent.left = y;
    } 
    else {
      x.parent.right = y;
    }

    y.left = x;
    x.parent = y;
  }

  public void right_rotation(Node x) {
    Node y = x.left;
    x.left = y.right;
    if (y.right != Null_Node) {
      y.right.parent = x;
    }
    y.parent = x.parent;
    if (x.parent == null) {
      this.root = y;
    } 
    else if (x == x.parent.right) {
      x.parent.right = y;
    } 
    else {
      x.parent.left = y;
    }
    y.right = x;
    x.parent = y;
  }

  public Node search(Node node, int key) {
    while(node != Null_Node) {
      if(node.value == key){
        return node;
      }
      if(node.value <= key) {
        node = node.right;
      } else {
        node = node.left;
      }
    }
    return Null_Node;
  }


  // Balance the tree after deletion of a node
  private void Tree_Balance_Delete(Node x) {
    Node sibling;
    while (x != root && x.color == 0) {
      //Finding the sibling of the node.
      if(x == x.parent.left){
        sibling = x.parent.right;
        if (sibling.color == 1) {
          sibling.color = 0;
          x.parent.color = 1;
          left_rotation(x.parent);
          sibling = x.parent.right;
        }
      }
      else{
        sibling = x.parent.left;
        if (sibling.color == 1) {
          sibling.color = 0;
          x.parent.color = 1;
          right_rotation(x.parent);
          sibling = x.parent.left;
        }
      }

      
      if (x == x.parent.left) {
        if (sibling.left.color == 0 && sibling.right.color == 0) {
          sibling.color = 1;
          x = x.parent;
        } 
        else if(sibling.right.color == 0){
          sibling.left.color = 0;
          sibling.color = 1;
          right_rotation(sibling);
          sibling = x.parent.right;
          sibling.color = x.parent.color;
          x.parent.color = 0;
          sibling.right.color = 0;
          left_rotation(x.parent);
          x = root;
        }
        else {
          sibling.color = x.parent.color;
          x.parent.color = 0;
          sibling.right.color = 0;
          left_rotation(x.parent);
          x = root;
        }
      } 
      else {
        if (sibling.left.color == 0 && sibling.right.color == 0) {
          sibling.color = 1;
          x = x.parent;
        }
        else if(sibling.left.color == 0){
          sibling.right.color = 0;
          sibling.color = 1;
          left_rotation(sibling);
          sibling = x.parent.left;
          sibling.color = x.parent.color;
          x.parent.color = 0;
          sibling.left.color = 0;
          right_rotation(x.parent);
          x = root;
        }
        else {
          sibling.color = x.parent.color;
          x.parent.color = 0;
          sibling.left.color = 0;
          right_rotation(x.parent);
          x = root;
        }
      }
    }
    x.color = 0;
  }

  public void delete_key(Node root_node,int key) {
    Node deleted_node = search(root_node,key);
    Node x;

    if (deleted_node == Null_Node) {
      System.out.println("The specified key doesn't exists in the tree.");
      return;
    }

    Node left_child = deleted_node.left;
    Node right_child = deleted_node.right;

    if (left_child == Null_Node) {
      
      if (deleted_node.parent == null) {
        root = right_child;
      } 
      else if (deleted_node == deleted_node.parent.left) {
        deleted_node.parent.left = right_child;
      } 
      else {
        deleted_node.parent.right = right_child;
      }
      right_child.parent = deleted_node.parent;

      if (deleted_node.color == 1) return;
      Tree_Balance_Delete(right_child);

    } 
    else if (right_child == Null_Node) {

      if (deleted_node.parent == null) {
        root = left_child;
      }
      else if (deleted_node == deleted_node.parent.left) {
        deleted_node.parent.left = left_child;
      } 
      else {
        deleted_node.parent.right = left_child;
      }
      left_child.parent = deleted_node.parent;

      if (deleted_node.color == 1) return;
      Tree_Balance_Delete(left_child);

    } 
    else {
      Node successor = right_child;
      while (successor.left != Null_Node) {
        successor = successor.left;
      }
      x = successor.right;

      deleted_node.value = successor.value;
      deleted_node = successor;
      Node right_deleted_node = deleted_node.right;

      if(deleted_node.color == 0 && right_deleted_node.color == 0){
        //Replacing successor with its right child.
        if (deleted_node.parent == null) {
          root = right_deleted_node;
        } 
        else if (deleted_node == deleted_node.parent.left) {
          deleted_node.parent.left = right_deleted_node;
        } 
        else {
          deleted_node.parent.right = right_deleted_node;
        }
        right_deleted_node.parent = deleted_node.parent;
        Tree_Balance_Delete(x);
      }
      else if(deleted_node.color == 0 && right_deleted_node.color == 1){
        if (deleted_node.parent == null) {
          root = right_deleted_node;
        } 
        else if (deleted_node == deleted_node.parent.left) {
          deleted_node.parent.left = right_deleted_node;
        } 
        else {
          deleted_node.parent.right = right_deleted_node;
        }
        right_deleted_node.parent = deleted_node.parent;
        x.color = 0;
      }
      else if(deleted_node.color == 1 && right_deleted_node.color == 0){
        if (deleted_node.parent == null) {
          root = right_deleted_node;
        } 
        else if (deleted_node == deleted_node.parent.left) {
          deleted_node.parent.left = right_deleted_node;
        } 
        else {
          deleted_node.parent.right = right_deleted_node;
        }
        right_deleted_node.parent = deleted_node.parent;
        x.color = 0;
      }
      else{
        if (deleted_node.parent == null) {
          root = right_deleted_node;
        } 
        else if (deleted_node == deleted_node.parent.left) {
          deleted_node.parent.left = right_deleted_node;
        } 
        else {
          deleted_node.parent.right = right_deleted_node;
        }
        right_deleted_node.parent = deleted_node.parent;
        x.color = 0;
      }
    }
  }

  // Balance the node after insertion
  private void Tree_Balance_Insert(Node k) {
    Node u;
    while (k.parent.color == 1) {
      if (k.parent == k.parent.parent.right) {
        u = k.parent.parent.left;
        if (u.color == 1) {
          u.color = 0;
          k.parent.color = 0;
          k.parent.parent.color = 1;
          k = k.parent.parent;
        } 
        else {
          if (k == k.parent.left) {
            k = k.parent;
            right_rotation(k);
          }
          k.parent.color = 0;
          k.parent.parent.color = 1;
          left_rotation(k.parent.parent);
        }
      } 
      else {
        u = k.parent.parent.right;

        if (u.color == 1) {
          u.color = 0;
          k.parent.color = 0;
          k.parent.parent.color = 1;
          k = k.parent.parent;
        } 
        else {
          if (k == k.parent.right) {
            k = k.parent;
            left_rotation(k);
          }
          k.parent.color = 0;
          k.parent.parent.color = 1;
          right_rotation(k.parent.parent);
        }
      }
      
      if (k.parent == null) {
        k.color = 0;
        return;
      }
      else if (k.parent.parent == null) {
        return;
      }
      else if (k.parent.color == 0) return ;

    }
    root.color = 0;
  }

  int node_count=1;

  public void Insert(int key) {
    //Creating a new node with Red color.
    Node new_node = new Node();
    new_node.parent = null;
    new_node.value = key;
    new_node.left = Null_Node;
    new_node.right = Null_Node;
    new_node.color = 1;
    new_node.label = node_count;
    node_count++;

    Node parent_node = null;
    Node current_node = this.root;

    //Finding the node to which it should be attached.
    while (current_node != Null_Node) {
      parent_node = current_node;
      if (key < current_node.value) {
        current_node = current_node.left;
      } else {
        current_node = current_node.right;
      }
    }

    new_node.parent = parent_node;
    //Parent node can be null.
    if (parent_node == null) {
      root = new_node;
    } 
    //Otherwise we will attach it as an appropriate child.
    else if (new_node.value < parent_node.value) {
      parent_node.left = new_node;
    } 
    else {
      parent_node.right = new_node;
    }

    //Color black if it is a root node and no fixing is required.
    if (new_node.parent == null) {
      new_node.color = 0;
      return;
    }

    //Color Red if it is the immediate child of the root node.
    if (new_node.parent.parent == null) {
      return;
    }

    //If the inserted node's parent is black then the tree is RBTree only.
    if (new_node.parent.color == 0) return ;

    //Color Red in other cases and fix the new tree.
    Tree_Balance_Insert(new_node);
  }

  public void print_tree_helper(Node root,FileWriter my_writer) {
    try{
      Stack<Node> stack = new Stack<Node>();
      stack.push(root);
      while(!stack.isEmpty()){
        Node temp = stack.pop();
        if (temp != Null_Node) {
          String sColor = temp.color == 1 ? "red" : "black";
          String name = "node"+Integer.toString(temp.label)+"[label = "+Integer.toString(temp.value)+" color =white fillcolor="+sColor+" style=filled ];\n";
          my_writer.write(name);
          stack.push(temp.right);
          stack.push(temp.left);
          if(temp.left!=Null_Node){
            my_writer.write("node"+Integer.toString(temp.label)+" -> node"+Integer.toString(temp.left.label)+" ;\n");
          }
          if(temp.right!=Null_Node){
            my_writer.write("node"+Integer.toString(temp.label)+" -> node"+Integer.toString(temp.right.label)+" ;\n");
          }
        }
      }
    }
    catch(Exception e){
      System.out.println("Exception in print_tree_helper function "+e);
    }
  }


  public void print_tree(Node root){
    try{
      File output_file = new File("output.dot");
      FileWriter my_writer = new FileWriter(output_file);
      my_writer.write("digraph BST {\n");
      my_writer.write("node [shape=circle, style=filled, color=white, fontname=Helvetica fontcolor=white]\n");
      my_writer.write("edge [fontname=Helvetica]\n");
      print_tree_helper(root, my_writer);
      my_writer.write("}\n");
      my_writer.close();
      //Execution shell commands to generate the .png files.
      String command = "dot -Tpng output.dot -o Graph.png";
      Runtime.getRuntime().exec(command);
    }
    catch(Exception e){
      System.out.println("Exception in print_tree function "+e);
    }
  }

  // Inorder Traversal 
  public void Inorder_Traversal(Node node) {
    Stack<Node> stack = new Stack<>();
    Node current = node;
  
    while (current != Null_Node || !stack.isEmpty()) {
      while (current != Null_Node) {
        stack.push(current);
        current = current.left;
      }
  
      current = stack.pop();
      System.out.print(current.value + " ");
      current = current.right;
    }
  }

  public void Delete(int data){
    delete_key(this.root, data);
  }

  public Node Search(int data){
    return search(this.root,data);
  }

  public void showGraph(){
    print_tree(this.root);
  }

  public void printInorder(){
    Inorder_Traversal(this.root);
  }

  public static void main(String[] args) {
    try{
      File input_file = new File("input.txt");
      Scanner my_scanner = new Scanner(input_file);
      
      int m;                                                  //Number of Operation to be performed.
      m=my_scanner.nextInt();

      RBTree rbt = new RBTree();
      for(int i=0;i<m;i++){
        int ind;                                              // 0 - To insert a key
                                                              // 1 - To delete a key
                                                              // 2 - To print the tree
          
        ind = my_scanner.nextInt();
        
        //Inserting a key.
        if(ind == 0){
          int key = my_scanner.nextInt();
          rbt.Insert(key);
        }
        //Deleting a key.
        else if(ind == 1){
          int data = my_scanner.nextInt();
          rbt.Delete(data);
        }
        //Searching a key.
        else if(ind == 2){
          int data = my_scanner.nextInt();
          Node temp = rbt.Search(data);
          if(temp == rbt.Null_Node) {
            System.out.println("The specified key is not present in the tree");
          }
          else{
            System.out.println("The specified key is present in the tree");
          }
        }
        //Printing the Tree.
        else if(ind == 3){
          rbt.showGraph();
        }
        //Print inorder traversal.
        else if(ind == 4){
          rbt.printInorder();
        }
        else{
          //Assisting to give the correct values.
          System.out.println("No operation is specified for the value: "+ind);
          System.out.println("0 : To insert the key");
          System.out.println("1 : To Delete the key");
          System.out.println("2 : To search a key");
          System.out.println("3 : To Print the Tree");
          System.out.println("4 : Inorder Traversal of the Tree");
        }
      }
      my_scanner.close();
      
    }
    catch(Exception e){
      System.out.println("Exception in main function "+e);
    }
  }
}