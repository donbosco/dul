/*     */ package pelib.scissors;
/*     */ 
/*     */ import pelib.Node;
/*     */ 
/*     */ class NodePriorityQueue
/*     */ {
/*     */   private Node[] data;
/*     */   private int size;
/*     */ 
/*     */   public NodePriorityQueue()
/*     */   {
/* 111 */     this.data = new Node[''];
/* 112 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   public void add(Node node)
/*     */   {
/* 119 */     if (this.size == this.data.length)
/*     */     {
/* 121 */       Node[] tmp = new Node[this.data.length * 2];
/* 122 */       System.arraycopy(this.data, 0, tmp, 0, this.size);
/* 123 */       this.data = tmp;
/*     */     }
/*     */ 
/* 127 */     int idx = this.size++;
/* 128 */     this.data[idx] = node;
/*     */ 
/* 130 */     while ((idx > 0) && (this.data[((idx - 1) / 2)].cost > node.cost))
/*     */     {
/* 132 */       this.data[idx] = this.data[((idx - 1) / 2)];
/* 133 */       idx = (idx - 1) / 2;
/*     */     }
/* 135 */     this.data[idx] = node;
/*     */   }
/*     */ 
/*     */   public Node first()
/*     */   {
/* 141 */     if (this.size == 0) {
/* 142 */       return null;
/*     */     }
/*     */ 
/* 145 */     Node ret = this.data[0];
/*     */ 
/* 148 */     Node node = this.data[(--this.size)];
/*     */     int child;
int idx;
/* 150 */     for ( idx = 0; 
/* 152 */       idx * 2 + 1 < this.size; idx = child)
/*     */     {
/* 154 */       child = idx * 2 + 1;
/* 155 */       if ((child + 1 < this.size) && (this.data[(child + 1)].cost < this.data[child].cost))
/* 156 */         child++;
/* 157 */       if (this.data[child].cost >= node.cost) break;
/* 158 */       this.data[idx] = this.data[child];
/*     */     }
/*     */ 
/* 162 */     this.data[idx] = node;
/*     */ 
/* 164 */     return ret;
/*     */   }
/*     */ 
/*     */   public boolean remove(Node node)
/*     */   {int idx;
/* 172 */     for ( idx = 0; (idx < this.size) && (this.data[idx] != node); idx++);
/* 176 */     if (idx >= this.size) {
/* 177 */       return false;
/*     */     }
/*     */ 
/* 180 */     node = this.data[(--this.size)];
/*     */     int child;
/* 182 */     for (; idx * 2 + 1 < this.size; idx = child)
/*     */     {
/* 185 */       child = idx * 2 + 1;
/* 186 */       if ((child + 1 < this.size) && (this.data[(child + 1)].cost < this.data[child].cost))
/* 187 */         child++;
/* 188 */       if (this.data[child].cost >= node.cost) break;
/* 189 */       this.data[idx] = this.data[child];
/*     */     }
/*     */ 
/* 193 */     this.data[idx] = node;
/*     */ 
/* 195 */     return true;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 200 */     return this.size;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 205 */     for (int i = 0; i < this.size; i++)
/* 206 */       this.data[i] = null;
/* 207 */     this.size = 0;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.scissors.NodePriorityQueue
 * JD-Core Version:    0.6.2
 */