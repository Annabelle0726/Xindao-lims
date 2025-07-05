<template>
  <div class="rating-widget">
    <div class="rating-header">
      <h3>请评价本次对话体验</h3>
      <p>您的反馈将帮助我们改进服务质量</p>
    </div>

    <div class="stars-container">
      <span
        v-for="star in 5"
        :key="star"
        class="star"
        :class="{ active: star <= selectedRating }"
        @click="selectRating(star)"
      >
        <i class="el-icon-star-on"></i>
      </span>
    </div>

    <button
      class="submit-btn"
      :disabled="selectedRating === 0"
      @click="submitRating"
    >
      提交评价
    </button>
  </div>
</template>

<script>
export default {
  name: 'RatingWidget',
  data() {
    return {
      selectedRating: 0
    };
  },
  methods: {
    selectRating(rating) {
      this.selectedRating = rating;
    },
    submitRating() {
      this.$emit('submit', this.selectedRating);
    }
  }
};
</script>

<style lang="scss" scoped>
.rating-widget {
  padding: 20px;
  text-align: center;
  background: #f8f9fa;
  border-top: 1px solid #eee;

  .rating-header {
    margin-bottom: 15px;

    h3 {
      margin: 0 0 5px 0;
      font-size: 18px;
      color: #333;
    }

    p {
      margin: 0;
      font-size: 14px;
      color: #999;
    }
  }

  .stars-container {
    margin-bottom: 20px;

    .star {
      display: inline-block;
      margin: 0 5px;
      font-size: 32px;
      color: #ccc;
      cursor: pointer;
      transition: all 0.2s;

      &.active {
        color: #ffc107;
        transform: scale(1.1);
      }

      &:hover {
        transform: scale(1.2);
      }
    }
  }

  .submit-btn {
    background: #409EFF;
    color: white;
    border: none;
    border-radius: 20px;
    padding: 8px 25px;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.3s;

    &:disabled {
      background: #c0c4cc;
      cursor: not-allowed;
    }

    &:not(:disabled):hover {
      background: #66b1ff;
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(64, 158, 255, 0.3);
    }
  }
}
</style>
