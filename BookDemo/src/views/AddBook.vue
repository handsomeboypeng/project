<template>
<!--  Form 组件提供了表单验证的功能，只需要通过 rules 属性传入约定的验证规则，
      并将 Form-Item 的 prop 属性设置为需校验的字段名即可。校验规则参见 async-validator-->
<!--  ruleForm对象用来绑定输入框的数据-->
<!--  ref="ruleForm" 映射formname-->
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm" style="width:40%">
        <el-form-item label="书名" prop="name">
            <el-input v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="作者" prop="author">
            <el-input v-model="ruleForm.author"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">添加</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
    </el-form>
</template>


<script>
    export default {
        data() {
            return {
                ruleForm: {
                    name: '',
                    author: ''
                },
                rules: {
                    name: [
                        { required: true, message: '请输入书名', trigger: 'blur' },
                        { min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }
                    ],
                    author: [
                        { required: true, message: '请输入作者', trigger: 'blur' },
                        { min: 2, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur' }
                    ]
                }
            };
        },
        methods: {
            submitForm(formName) {
                var _this = this;
                this.$refs[formName].validate((valid) => {
                  //valid为true表示数据通过了校验
                    if (valid) {
                        axios.post("http://localhost:8181/book/save",this.ruleForm).then(function (resp) {
                            // console.log(resp)
                            if(resp.data == 'success'){
                                _this.$alert('添加成功', '提示', {
                                    confirmButtonText: '确定',
                                    callback: action => {
                                        _this.$router.push("/manage")
                                    }
                                });
                            }else{
                                this.$message('添加失败')
                            }
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }
    }
</script>
