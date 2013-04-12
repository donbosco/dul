/*     */ package pelib.scissors;
/*     */ 
/*     */ class DijkstraPriorityQueue
/*     */ {
/*     */   private DijkstraNode[] data;
/*     */   private int size;
/*     */ 
/*     */   public DijkstraPriorityQueue()
/*     */   {
/* 143 */     this.data = new DijkstraNode[''];
/* 144 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   public void add(DijkstraNode node)
/*     */   {
/* 150 */     if (this.size == this.data.length)
/*     */     {
/* 152 */       DijkstraNode[] tmp = new DijkstraNode[this.data.length * 2];
/* 153 */       System.arraycopy(this.data, 0, tmp, 0, this.size);
/* 154 */       this.data = tmp;
/*     */     }
/*     */ 
/* 158 */     int idx = this.size++;
/* 159 */     this.data[idx] = node;
/*     */ 
/* 161 */     while ((idx > 0) && (this.data[((idx - 1) / 2)].cost > node.cost))
/*     */     {
/* 163 */       this.data[idx] = this.data[((idx - 1) / 2)];
/* 164 */       idx = (idx - 1) / 2;
/*     */     }
/* 166 */     this.data[idx] = node;
/*     */   }
/*     */ 
/*     */   public DijkstraNode first()
/*     */   {
/* 172 */     if (this.size == 0) {
/* 173 */       return null;
/*     */     }
/*     */ 
/* 176 */     DijkstraNode ret = this.data[0];
/*     */ 
/* 179 */     DijkstraNode node = this.data[(--this.size)];
/*     */     int child;int idx;
/* 181 */     for ( idx = 0; 
/* 183 */       idx * 2 + 1 < this.size; idx = child)
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
/* 195 */     return ret;
/*     */   }
/*     */ 
/*     */   public void remove(DijkstraNode node)
/*     */   {int idx;
/* 202 */     for ( idx = 0; (idx < this.size) && (this.data[idx] != node); idx++);
/* 206 */     if (idx >= this.size) {
/* 207 */       return;
/*     */     }
/*     */ 
/* 210 */     node = this.data[(--this.size)];
/*     */     int child;
/* 212 */     for (; idx * 2 + 1 < this.size; idx = child)
/*     */     {
/* 214 */       child = idx * 2 + 1;
/* 215 */       if ((child + 1 < this.size) && (this.data[(child + 1)].cost < this.data[child].cost))
/* 216 */         child++;
/* 217 */       if (this.data[child].cost >= node.cost) break;
/* 218 */       this.data[idx] = this.data[child];
/*     */     }
/*     */ 
/* 222 */     this.data[idx] = node;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 228 */     return this.size;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 233 */     for (int i = 0; i < this.size; i++)
/* 234 */       this.data[i] = null;
/* 235 */     this.size = 0;
/*     */   }
/*     */ }

/* Location:           dulux-signed.jar
 * Qualified Name:     pelib.scissors.DijkstraPriorityQueue
 * JD-Core Version:    0.6.2
 */